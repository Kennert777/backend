package com.alimentandoofuturo.backend.controller;

import com.alimentandoofuturo.backend.model.entity.Horta;
import com.alimentandoofuturo.backend.model.service.HortaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hortas")
@CrossOrigin(origins = "*")
public class HortaController {

    private final HortaService service;

    public HortaController(HortaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Horta> listar() {
        return service.listarTodas();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Horta> listarPorUsuario(@PathVariable Long usuarioId) {
        return service.listarPorUsuario(usuarioId);
    }

    @PostMapping
    public ResponseEntity<Horta> salvar(@RequestBody Horta horta) {
        return ResponseEntity.ok(service.salvar(horta));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Horta> buscar(@PathVariable Long id) {
        Horta horta = service.buscarPorId(id);
        return horta != null ? ResponseEntity.ok(horta) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Horta horta) {
        try {
            Horta hortaAtualizada = service.atualizar(id, horta);
            return ResponseEntity.ok(hortaAtualizada);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", e.getMessage()));
        }
    }

    @PutMapping("/{id}/aprovar")
    public ResponseEntity<?> aprovar(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            Object adminIdObj = request.get("adminId");
            Long adminId = adminIdObj instanceof Integer ? ((Integer) adminIdObj).longValue() : (Long) adminIdObj;
            Horta hortaAprovada = service.aprovar(id, adminId);
            return ResponseEntity.ok(hortaAprovada);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", e.getMessage()));
        }
    }

    @PutMapping("/{id}/rejeitar")
    public ResponseEntity<?> rejeitar(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            Object adminIdObj = request.get("adminId");
            Long adminId = adminIdObj instanceof Integer ? ((Integer) adminIdObj).longValue() : (Long) adminIdObj;
            String motivo = (String) request.get("motivo");
            Horta hortaRejeitada = service.rejeitar(id, adminId, motivo);
            return ResponseEntity.ok(hortaRejeitada);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            service.deletar(id);
            return ResponseEntity.ok(Map.of("msg", "Horta deletada"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", e.getMessage()));
        }
    }
}
