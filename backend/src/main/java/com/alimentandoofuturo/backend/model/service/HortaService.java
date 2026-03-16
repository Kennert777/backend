package com.alimentandoofuturo.backend.model.service;

import com.alimentandoofuturo.backend.model.entity.Horta;
import com.alimentandoofuturo.backend.model.enums.StatusHorta;
import com.alimentandoofuturo.backend.model.repository.HortaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HortaService {

    private final HortaRepository repository;

    public HortaService(HortaRepository repository) {
        this.repository = repository;
    }

    public List<Horta> listarTodas() {
        return repository.findByAtivoTrue();
    }

    public Horta buscarPorId(Long id) {
        return repository.findByIdAndAtivoTrue(id).orElse(null);
    }

    public Horta salvar(Horta horta) {
        return repository.save(horta);
    }

    public Horta atualizar(Long id, Horta horta) {
        Horta hortaExistente = repository.findById(id).orElseThrow(
                () -> new RuntimeException("Horta não encontrada com ID: " + id)
        );
        
        // Update only provided fields
        if (horta.getNome() != null) hortaExistente.setNome(horta.getNome());
        if (horta.getDescricao() != null) hortaExistente.setDescricao(horta.getDescricao());
        if (horta.getLocalizacao() != null) hortaExistente.setLocalizacao(horta.getLocalizacao());
        if (horta.getTipoCultivo() != null) hortaExistente.setTipoCultivo(horta.getTipoCultivo());
        if (horta.getAreaM2() != null) hortaExistente.setAreaM2(horta.getAreaM2());
        if (horta.getCapacidadePessoas() != null) hortaExistente.setCapacidadePessoas(horta.getCapacidadePessoas());
        if (horta.getStatus() != null) hortaExistente.setStatus(horta.getStatus());
        if (horta.getLatitude() != null) hortaExistente.setLatitude(horta.getLatitude());
        if (horta.getLongitude() != null) hortaExistente.setLongitude(horta.getLongitude());
        
        hortaExistente.setDataUltimaAtualizacao(java.time.LocalDateTime.now());
        
        return repository.save(hortaExistente);
    }

    public void deletar(Long id) {
        Horta horta = repository.findById(id).orElseThrow(
                () -> new RuntimeException("Horta não encontrada com ID: " + id)
        );
        horta.setAtivo(false);
        repository.save(horta);
    }

    public List<Horta> listarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioResponsavelIdAndAtivoTrue(usuarioId);
    }

    public Horta aprovar(Long id, Long adminId) {
        Horta horta = repository.findById(id).orElseThrow(
                () -> new RuntimeException("Horta não encontrada com ID: " + id)
        );
        
        horta.setAprovada(true);
        horta.setDataAprovacao(java.time.LocalDateTime.now());
        horta.setAdminAprovadorId(adminId);
        
        return repository.save(horta);
    }

    public Horta rejeitar(Long id, Long adminId, String motivo) {
        Horta horta = repository.findById(id).orElseThrow(
                () -> new RuntimeException("Horta não encontrada com ID: " + id)
        );
        
        horta.setAprovada(false);
        horta.setStatus(StatusHorta.PLANEJAMENTO);
        horta.setMotivoRejeicao(motivo);
        horta.setDataUltimaAtualizacao(java.time.LocalDateTime.now());
        horta.setAdminAprovadorId(adminId);
        
        return repository.save(horta);
    }
}
