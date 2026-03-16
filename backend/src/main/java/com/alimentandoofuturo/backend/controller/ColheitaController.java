package com.alimentandoofuturo.backend.controller;

import com.alimentandoofuturo.backend.model.entity.Colheita;
import com.alimentandoofuturo.backend.model.service.ColheitaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/colheitas")
@CrossOrigin(origins = "*")
public class ColheitaController {

    private final ColheitaService service;

    public ColheitaController(ColheitaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Colheita> listar() {
        return service.listarTodas();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Colheita> listarPorUsuario(@PathVariable Long usuarioId) {
        return service.listarPorUsuario(usuarioId);
    }

    @GetMapping("/horta/{hortaId}")
    public List<Colheita> listarPorHorta(@PathVariable Long hortaId) {
        return service.listarPorHorta(hortaId);
    }

    @PostMapping
    public ResponseEntity<Colheita> salvar(@RequestBody Colheita colheita) {
        return ResponseEntity.ok(service.salvar(colheita));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Colheita> buscar(@PathVariable Long id) {
        Colheita colheita = service.buscarPorId(id);
        return colheita != null ? ResponseEntity.ok(colheita) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Colheita colheita) {
        try {
            Colheita colheitaAtualizada = service.atualizar(id, colheita);
            return ResponseEntity.ok(colheitaAtualizada);
        } catch (Exception e) {
            System.out.println("Erro ao atualizar colheita: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("erro", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            service.deletar(id);
            return ResponseEntity.ok(Map.of("msg", "Colheita deletada"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", e.getMessage()));
        }
    }
}
