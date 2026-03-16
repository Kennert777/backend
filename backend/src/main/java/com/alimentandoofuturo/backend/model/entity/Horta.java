package com.alimentandoofuturo.backend.model.entity;

import com.alimentandoofuturo.backend.model.enums.StatusHorta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Horta")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Horta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Column(nullable = false)
    private String localizacao;

    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "tipo_cultivo", nullable = false, length = 20)
    private String tipoCultivo;

    @Column(name = "area_m2", precision = 8, scale = 2)
    private BigDecimal areaM2;

    @Column(name = "capacidade_pessoas")
    private Integer capacidadePessoas;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private StatusHorta status = StatusHorta.PLANEJAMENTO;

    @Column(name = "data_criacao")
    @Builder.Default
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "data_ultima_atualizacao")
    @Builder.Default
    private LocalDateTime dataUltimaAtualizacao = LocalDateTime.now();

    @Builder.Default
    private Boolean aprovada = false;

    @Column(name = "data_aprovacao")
    private LocalDateTime dataAprovacao;

    @Column(name = "usuario_responsavel_id", nullable = false)
    private Long usuarioResponsavelId;

    @Column(name = "admin_aprovador_id")
    private Long adminAprovadorId;

    @Column(name = "motivo_rejeicao", length = 500)
    private String motivoRejeicao;

    @Builder.Default
    private Boolean ativo = true;
}
