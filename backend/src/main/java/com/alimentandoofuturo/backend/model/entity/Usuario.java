package com.alimentandoofuturo.backend.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Usuario")
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String telefone;

    @Column(nullable = false)
    private String senha;

    @Column(name = "tipo_perfil", length = 20)
    @Builder.Default
    private String tipoPerfil = "USUARIO";

    @Builder.Default
    private Integer pontos = 0;

    @Builder.Default
    private Integer nivel = 1;

    @Column(name = "data_cadastro")
    @Builder.Default
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @Column(name = "data_ultimo_acesso")
    private LocalDateTime dataUltimoAcesso;

    @Builder.Default
    private Boolean ativo = true;

    private String endereco;

    @Column(length = 100)
    private String cidade;

    @Column(length = 50)
    private String estado;
}