package com.alimentandoofuturo.backend.model.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private UsuarioDto usuario;
    private String token;

    @Data
    public static class UsuarioDto {
        private Long id;
        private String nome;
        private String email;
        private String tipoPerfil;
        private Boolean ativo;
    }
}