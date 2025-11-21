package com.api.pjpPequenosPassos.controller;

import com.api.pjpPequenosPassos.service.RadarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/radar")
@CrossOrigin(origins = "*")
public class RadarController {

    private final RadarService radarService;

    public RadarController(RadarService radarService) {
        this.radarService = radarService;
    }

    // Radar (só progresso)
    @GetMapping("/{criancaId}")
    public ResponseEntity<Map<String, Object>> gerarRadar(@PathVariable Long criancaId) {
        Map<String, Object> radar = radarService.gerarRadar(criancaId);
        return ResponseEntity.ok(radar);
    }

    // Análise via IA (somente quando o botão é clicado)
    @GetMapping("/{criancaId}/analise-ia")
    public ResponseEntity<List<String>> gerarAnaliseIA(@PathVariable Long criancaId) {
        List<String> recomendacoes = radarService.gerarAnaliseIA(criancaId);
        return ResponseEntity.ok(recomendacoes);
    }
}

