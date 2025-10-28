package com.teste.spring.teste.dto;

import jakarta.validation.constraints.*;

public class ClienteDto {
    private Long id;

    @NotBlank @Size(max = 120)
    private String nome;

    @NotBlank @Email @Size(max = 160)
    private String email;

    @Size(max = 20)
    private String telefone;

    // getters/setters
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; } public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; } public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; } public void setTelefone(String telefone) { this.telefone = telefone; }
}
