package com.eduardo.stockify.config.security;

import com.eduardo.stockify.repositories.UsuarioRepository;
import com.eduardo.stockify.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = recuperarToken(request);

            if (token != null) {
                try {

                    String subject = tokenService.getSubject(token);

                    var usuario = usuarioRepository.findByUsername(subject);
                    if (usuario != null && usuario.isEnabled()) {
                        var authentication = new UsernamePasswordAuthenticationToken(
                                usuario, null, usuario.getAuthorities());

                        var context = SecurityContextHolder.createEmptyContext();
                        context.setAuthentication(authentication);
                        SecurityContextHolder.setContext(context);
                    }

                } catch (Exception ex) {
                    log.debug("Falha ao validar JWT: {}", ex.getMessage(), ex);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String value = request.getHeader(AUTH_HEADER);
        if (value == null) return null;

        value = value.trim();

        if (value.regionMatches(true, 0, BEARER_PREFIX, 0, BEARER_PREFIX.length())) {
            String token = value.substring(BEARER_PREFIX.length()).trim();
            return token.isEmpty() ? null : token;
        }

        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        return path.startsWith("/auth/")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || "/swagger-ui.html".equals(path)
                || "/actuator/health".equals(path);
    }

}
