package com.seuapp.config;

import com.seuapp.repository.UserRepository;
import com.seuapp.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        
        if (token != null) {
            // Se o token existe, validamos usando o serviço que criamos antes
            var email = tokenService.validarToken(token);
            
            // Se retornar um email, o token é válido
            if (email != null && !email.isEmpty()) {
                var user = userRepository.findByEmail(email).orElse(null);
                
                if (user != null) {
                    // Como ainda não implementamos Roles (Cargos/Permissões), passamos null nas authorities
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, null);
                    // Salva a autenticação no contexto do Spring Security
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        
        // Continua o fluxo da requisição
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        
        // O token padrão vem no formato "Bearer asdfghjkl...", então removemos a palavra "Bearer "
        return authHeader.replace("Bearer ", "");
    }
}