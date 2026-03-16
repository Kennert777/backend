package com.alimentandoofuturo.backend.controller;

import com.alimentandoofuturo.backend.model.entity.SupportRequest;
import com.alimentandoofuturo.backend.model.service.SupportRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/suporte")
@CrossOrigin(origins = {"http://localhost:5173", "https://alimentando-o-futuro.netlify.app"})
public class SupportRequestController {

    private final SupportRequestService service;

    public SupportRequestController(SupportRequestService service) {
        this.service = service;
    }

    @GetMapping
    public List<SupportRequest> listar() {
        return service.listarTodos();
    }

    @PostMapping
    public SupportRequest salvar(@RequestBody SupportRequest supportRequest) {
        return service.salvar(supportRequest);
    }

    @GetMapping("/{id}")
    public SupportRequest buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/usuario/{id}")
    public List<SupportRequest> buscarPorUsuario(@PathVariable Long id) {
        return service.buscarPorUsuario(id);
    }

    @GetMapping("/status/{status}")
    public List<SupportRequest> buscarPorStatus(@PathVariable String status) {
        return service.buscarPorStatus(status);
    }

    @PutMapping("/{id}")
    public SupportRequest atualizar(@PathVariable Long id, @RequestBody SupportRequest supportRequest) {
        return service.atualizar(id, supportRequest);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        SupportRequest request = service.buscarPorId(id);
        if (request == null) {
            return ResponseEntity.notFound().build();
        }
        request.setStatus(body.get("status"));
        SupportRequest updated = service.atualizar(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(Map.of("msg", "Solicitação deletada"));
    }
}