package com.alimentandoofuturo.backend.controller;

import com.alimentandoofuturo.backend.model.service.ColheitaService;
import com.alimentandoofuturo.backend.model.service.HortaService;
import com.alimentandoofuturo.backend.model.service.UsuarioService;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/estatisticas")
@CrossOrigin(origins = "*")
public class EstatisticasController {

    private final HortaService hortaService;
    private final ColheitaService colheitaService;
    private final UsuarioService usuarioService;

    public EstatisticasController(HortaService hortaService, ColheitaService colheitaService, UsuarioService usuarioService) {
        this.hortaService = hortaService;
        this.colheitaService = colheitaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/usuario/{usuarioId}")
    public Map<String, Object> obterEstatisticasUsuario(@PathVariable Long usuarioId) {
        var hortas = hortaService.listarPorUsuario(usuarioId);
        var colheitas = colheitaService.listarPorUsuario(usuarioId);
        
        BigDecimal totalKg = colheitas.stream()
            .map(c -> c.getQuantidadeKg())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        long hortasAprovadas = hortas.stream().filter(h -> h.getAprovada()).count();
        long hortasPendentes = hortas.stream().filter(h -> !h.getAprovada()).count();
            
        return Map.of(
            "totalHortas", hortas.size(),
            "totalColheitas", colheitas.size(),
            "totalKgColhidos", totalKg,
            "hortasAprovadas", hortasAprovadas,
            "hortasPendentes", hortasPendentes
        );
    }

    @GetMapping("/admin/geral")
    public Map<String, Object> obterEstatisticasGerais() {
        var todosUsuarios = usuarioService.listarTodos();
        var todasHortas = hortaService.listarTodas();
        var todasColheitas = colheitaService.listarTodas();
        
        BigDecimal producaoTotal = todasColheitas.stream()
            .map(c -> c.getQuantidadeKg())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        long hortasAprovadas = todasHortas.stream().filter(h -> h.getAprovada()).count();
        long hortasPendentes = todasHortas.stream().filter(h -> !h.getAprovada()).count();
        long usuariosAtivos = todosUsuarios.stream().filter(u -> u.getAtivo()).count();
            
        return Map.of(
            "totalUsuarios", todosUsuarios.size(),
            "usuariosAtivos", usuariosAtivos,
            "totalHortas", todasHortas.size(),
            "hortasAprovadas", hortasAprovadas,
            "hortasPendentes", hortasPendentes,
            "totalColheitas", todasColheitas.size(),
            "producaoTotal", producaoTotal
        );
    }
}