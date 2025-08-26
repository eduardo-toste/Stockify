package com.eduardo.stockify.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.eduardo.stockify.utils.ProblemDetailsUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // === Custom Exceptions ===

    @ExceptionHandler(UsuarioExistenteException.class)
    public ResponseEntity<ProblemDetail> handleUsuarioExistente(UsuarioExistenteException ex, HttpServletRequest req) {
        var body = ProblemDetailsUtils.build(
                HttpStatus.CONFLICT, "Conflito", ex.getMessage(), UsuarioExistenteException.CODE, req.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        var body = ProblemDetailsUtils.build(
                HttpStatus.NOT_FOUND, "Não encontrado", ex.getMessage(), ResourceNotFoundException.CODE, req.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(EstoqueInsuficienteException.class)
    public ResponseEntity<ProblemDetail> handleEstoqueInsuficiente(EstoqueInsuficienteException ex, HttpServletRequest req) {
        var body = ProblemDetailsUtils.build(
                HttpStatus.CONFLICT, "Conflito", ex.getMessage(), EstoqueInsuficienteException.CODE, req.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    // === Auth ===

    @ExceptionHandler({ UsernameNotFoundException.class, BadCredentialsException.class, AuthenticationException.class })
    public ResponseEntity<ProblemDetail> handleAuth(RuntimeException ex, HttpServletRequest req) {
        var body = ProblemDetailsUtils.build(
                HttpStatus.UNAUTHORIZED, "Não autenticado", "Credenciais inválidas", "AUTH_FAILED", req.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        var body = ProblemDetailsUtils.build(
                HttpStatus.FORBIDDEN, "Acesso negado", "Você não tem permissão para acessar este recurso.",
                "ACCESS_DENIED", req.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    // === Validação ===

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<Map<String, Object>> violations = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toViolation)
                .toList();

        var body = ProblemDetailsUtils.build(
                HttpStatus.UNPROCESSABLE_ENTITY, "Erro de validação", "Campos inválidos.", "VALIDATION_ERROR", req.getRequestURI());
        body.setProperty("violations", violations);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ProblemDetail> handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest req) {
        var body = ProblemDetailsUtils.build(
                HttpStatus.BAD_REQUEST, "Parâmetro ausente",
                "O parâmetro obrigatório '" + ex.getParameterName() + "' não foi informado.",
                "MISSING_PARAMETER", req.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<ProblemDetail> handleInvalidJwt(JWTVerificationException ex, HttpServletRequest req) {
        String detail = switch (ex.getMessage()) {
            case "Refresh token ausente" -> "Refresh token ausente";
            case "Tipo de token inválido!" -> "Tipo de token inválido!";
            default -> "Token inválido ou expirado";
        };

        var body = ProblemDetailsUtils.build(
                HttpStatus.UNAUTHORIZED,
                "Não autenticado",
                detail,
                "AUTH_FAILED",
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest req) {
        var body = ProblemDetailsUtils.build(
                HttpStatus.UNAUTHORIZED,
                "Não autenticado",
                ex.getMessage(),
                "AUTH_FAILED",
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    // === JSON malformado ou enum inválido ===

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleJsonError(HttpMessageNotReadableException ex, HttpServletRequest req) {
        String detail = "Erro na leitura da requisição.";
        String code = "MALFORMED_JSON";

        if (ex.getMessage() != null && ex.getMessage().contains("com.eduardo.stockify.models.enums.Categoria")) {
            detail = "Valor inválido para o campo 'categoria'. Valores permitidos: OUTROS, ELETRONICO, ALIMENTO, VESTUARIO, LIVRO.";
            code = "ENUM_INVALID";
        }

        var body = ProblemDetailsUtils.build(
                HttpStatus.BAD_REQUEST, "Requisição inválida", detail, code, req.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // === Conflito no banco ===

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleConflict(DataIntegrityViolationException ex, HttpServletRequest req) {
        var body = ProblemDetailsUtils.build(
                HttpStatus.CONFLICT, "Conflito de dados", "Violação de integridade no banco de dados.",
                "DATA_INTEGRITY_VIOLATION", req.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    // === Erro genérico 500 ===

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneric(Exception ex, HttpServletRequest req) {
        var body = ProblemDetailsUtils.build(
                HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno",
                "Ocorreu um erro inesperado. Tente novamente mais tarde.",
                "INTERNAL_ERROR", req.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    // === Helper ===

    private Map<String, Object> toViolation(FieldError fe) {
        return Map.of("field", fe.getField(), "message", fe.getDefaultMessage());
    }
}