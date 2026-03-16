package com.alimentandoofuturo.backend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Colheita")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Colheita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "horta_id", nullable = false)
    private Long hortaId;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "tipo_planta", nullable = false, length = 100)
    private String tipoPlanta;

    @Column(name = "quantidade_kg", nullable = false, precision = 8, scale = 2)
    private BigDecimal quantidadeKg;

    @Column(name = "data_colheita", nullable = false)
    private LocalDate dataColheita;

    @Column(name = "data_registro")
    @Builder.Default
    private LocalDateTime dataRegistro = LocalDateTime.now();

    @Column(length = 20)
    private String qualidade;

    private String destino;

    @Column(length = 500)
    private String observacoes;

    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    @Builder.Default
    private Boolean ativo = true;

    public Object getHorta() {
      
        throw new UnsupportedOperationException("Unimplemented method 'getHorta'");
    }
}
