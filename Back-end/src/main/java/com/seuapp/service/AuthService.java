package com.seuapp.service;

import com.seuapp.dto.RegisterDTO;
import com.seuapp.model.User;
import com.seuapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registrar(RegisterDTO dto) {
        User user = new User();
        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());
        
        String senhaBCrypt = passwordEncoder.encode(dto.getSenha());
        user.setSenha(senhaBCrypt);

        return userRepository.save(user);
    }

    public Optional<User> login(String email, String senha) {
        return userRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(senha, u.getSenha()));
    }
}