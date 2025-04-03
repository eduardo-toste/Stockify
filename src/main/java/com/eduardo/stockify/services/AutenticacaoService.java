package com.eduardo.stockify.services;

import com.eduardo.stockify.dtos.AutenticacaoRequest;
import com.eduardo.stockify.exceptions.UsuarioExistenteException;
import com.eduardo.stockify.models.Usuario;
import com.eduardo.stockify.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username);
    }

    public void registrar(AutenticacaoRequest dados) {
        var usuario = repository.findByUsername(dados.username());

        if(usuario != null){
            throw new UsuarioExistenteException("Este usuário já foi cadastrado!");
        }

        var senhaCriptografada = passwordEncoder.encode(dados.password());

        repository.save(new Usuario(null, dados.username(), senhaCriptografada));
    }
}
