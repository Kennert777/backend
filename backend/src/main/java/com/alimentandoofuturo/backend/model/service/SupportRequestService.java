package com.alimentandoofuturo.backend.model.service;

import com.alimentandoofuturo.backend.model.entity.SupportRequest;
import com.alimentandoofuturo.backend.model.repository.SupportRequestRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SupportRequestService {

    private final SupportRequestRepository repository;
    private final EmailService emailService;

    public SupportRequestService(SupportRequestRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    public List<SupportRequest> listarTodos() {
        return repository.findByAtivoTrue();
    }

    public SupportRequest buscarPorId(Long id) {
        return repository.findByIdAndAtivoTrue(id).orElse(null);
    }

    public SupportRequest salvar(SupportRequest supportRequest) {
        SupportRequest saved = repository.save(supportRequest);
        try {
            emailService.enviarEmailSuporte(
                supportRequest.getNome(),
                supportRequest.getEmail(),
                supportRequest.getAssunto(),
                supportRequest.getMensagem()
            );
        } catch (Exception e) {
            // Log do erro, mas não falha a operação
        }
        return saved;
    }

    public SupportRequest atualizar(Long id, SupportRequest supportRequest) {
        SupportRequest requestExistente = repository.findById(id).orElseThrow(
                () -> new RuntimeException("Solicitação de suporte não encontrada")
        );
        supportRequest.setId(requestExistente.getId());
        return repository.save(supportRequest);
    }

    public void deletar(Long id) {
        SupportRequest request = repository.findById(id).orElseThrow(
                () -> new RuntimeException("Solicitação de suporte não encontrada")
        );
        request.setAtivo(false);
        repository.save(request);
    }

    public List<SupportRequest> buscarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioIdAndAtivoTrue(usuarioId);
    }

    public List<SupportRequest> buscarPorEmail(String email) {
        return repository.findByEmailAndAtivoTrue(email);
    }

    public List<SupportRequest> buscarPorStatus(String status) {
        return repository.findByStatusAndAtivoTrue(status);
    }
}