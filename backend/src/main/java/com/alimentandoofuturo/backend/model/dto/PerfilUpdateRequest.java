package com.alimentandoofuturo.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfilUpdateRequest {
    private String nome;
    private String email;
    private String senha;
}
