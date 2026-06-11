package com.seuapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginDTO {
    
    @NotBlank(message = "O campo e-mail não pode estar vazio.")
    @Email(message = "O formato do e-mail é inválido.")
    private String email;

    @NotBlank(message = "O campo senha não pode estar vazio.")
    @Size(min = 6, message = "A senha deve conter no mínimo 6 caracteres.")
    private String senha;

    // Getters e Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}