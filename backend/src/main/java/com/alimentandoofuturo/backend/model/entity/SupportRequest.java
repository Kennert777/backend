package com.alimentandoofuturo.backend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "support_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String email;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(nullable = false, length = 500)
    private String assunto;

    @Column(nullable = false, columnDefinition = "NTEXT")
    private String mensagem;

    @Column(name = "data_criacao")
    @Builder.Default
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(length = 50)
    @Builder.Default
    private String status = "ABERTO";

    @Column(columnDefinition = "NTEXT")
    private String resposta;

    @Column(name = "data_resposta")
    private LocalDateTime dataResposta;

    @Builder.Default
    private Boolean ativo = true;

    @ManyToOne
    @JoinColumn(name = "usuario_id", insertable = false, updatable = false)
    private Usuario usuario;
}