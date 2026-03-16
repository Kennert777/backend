package com.alimentandoofuturo.backend.model.service;

import com.alimentandoofuturo.backend.model.entity.Colheita;
import com.alimentandoofuturo.backend.model.repository.ColheitaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ColheitaService {

    private final ColheitaRepository repository;

    public ColheitaService(ColheitaRepository repository) {
        this.repository = repository;
    }

    public List<Colheita> listarTodas() {
        return repository.findByAtivoTrue();
    }

    public Colheita buscarPorId(Long id) {
        return repository.findByIdAndAtivoTrue(id).orElse(null);
    }

    public Colheita salvar(Colheita colheita) {
        return repository.save(colheita);
    }

    public Colheita atualizar(Long id, Colheita colheita) {
        Colheita colheitaExistente = repository.findById(id).orElseThrow(
                () -> new RuntimeException("Colheita não encontrada com ID: " + id)
        );
        
        // Update only provided fields
        if (colheita.getHortaId() != null) colheitaExistente.setHortaId(colheita.getHortaId());
        if (colheita.getUsuarioId() != null) colheitaExistente.setUsuarioId(colheita.getUsuarioId());
        if (colheita.getTipoPlanta() != null) colheitaExistente.setTipoPlanta(colheita.getTipoPlanta());
        if (colheita.getQuantidadeKg() != null) colheitaExistente.setQuantidadeKg(colheita.getQuantidadeKg());
        if (colheita.getDataColheita() != null) colheitaExistente.setDataColheita(colheita.getDataColheita());
        if (colheita.getQualidade() != null) colheitaExistente.setQualidade(colheita.getQualidade());
        if (colheita.getDestino() != null) colheitaExistente.setDestino(colheita.getDestino());
        if (colheita.getObservacoes() != null) colheitaExistente.setObservacoes(colheita.getObservacoes());
        if (colheita.getFotoUrl() != null) colheitaExistente.setFotoUrl(colheita.getFotoUrl());
        
        return repository.save(colheitaExistente);
    }

    public void deletar(Long id) {
        Colheita colheita = repository.findById(id).orElseThrow(
                () -> new RuntimeException("Colheita não encontrada com ID: " + id)
        );
        colheita.setAtivo(false);
        repository.save(colheita);
    }

    public List<Colheita> listarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioIdAndAtivoTrue(usuarioId);
    }

    public List<Colheita> listarPorHorta(Long hortaId) {
        return repository.findByHortaIdAndAtivoTrue(hortaId);
    }
}
