package com.eduardo.stockify.services;

import com.eduardo.stockify.dtos.AutenticacaoRequest;
import com.eduardo.stockify.exceptions.UsuarioExistenteException;
import com.eduardo.stockify.models.Usuario;
import com.eduardo.stockify.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutenticacaoService implements UserDetailsService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado no sistema"));
    }

    public void registrar(AutenticacaoRequest dados) {
        if(repository.existsByUsername(dados.username())) {
            throw new UsuarioExistenteException("Este usuário já foi cadastrado!");
        }

        var senhaCriptografada = passwordEncoder.encode(dados.password());

        repository.save(new Usuario(null, dados.username(), senhaCriptografada));
    }
}
