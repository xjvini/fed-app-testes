package com.seuapp.controller;

import com.seuapp.dto.LoginDTO;
import com.seuapp.dto.RegisterDTO;
import com.seuapp.model.User;
import com.seuapp.repository.UserRepository;
import com.seuapp.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.seuapp.service.TokenService;
import java.util.Map;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") 
public class AuthController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegisterDTO dto) {
        // Verifica se o e-mail já existe antes de tentar registrar
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Erro: Email já cadastrado!");
        }
        
        // Em um cenário real, você não deve retornar a entidade User crua
        // especialmente com a senha. Idealmente, retorne um UserResponseDTO.
        User novoUsuario = authService.registrar(dto);
        return ResponseEntity.ok(novoUsuario);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO dto) {
        Optional<User> userOptional = authService.login(dto.getEmail(), dto.getSenha());
        
        if (userOptional.isEmpty()) {
            // Retorna 401 Unauthorized se as credenciais forem inválidas
            return ResponseEntity.status(401).body("Erro: Email ou senha incorretos.");
        }

        User user = userOptional.get();
        String token = tokenService.gerarToken(user);
        return ResponseEntity.ok(Map.of("token", token));
    }
}