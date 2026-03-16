package com.alimentandoofuturo.backend.model.dto;

import lombok.Data;

@Data
public class UsuarioUpdateRequest {
    private Long id;
    private String nome;
    private String email;
    private String tipoPerfil;
    private Boolean ativo;
    private String senha;
    private String telefone;
    private String cidade;
    private String endereco;
    private String estado;
}