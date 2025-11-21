package com.api.pjpPequenosPassos.controller;

import com.api.pjpPequenosPassos.model.AvaliacaoRisco;
import com.api.pjpPequenosPassos.service.AvaliacaoRiscoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/avaliacoes-risco")
@CrossOrigin(origins = "*")
public class AvaliacaoRiscoController {

    @Autowired
    private AvaliacaoRiscoService avaliacaoRiscoService;

    // ==================== CRIAR AVALIAÇÃO [RF017] ====================
    @PostMapping
    public ResponseEntity<AvaliacaoRisco> criarAvaliacao(@Valid @RequestBody AvaliacaoRisco avaliacao) {
        try {
            AvaliacaoRisco novaAvaliacao = avaliacaoRiscoService.criarAvaliacao(avaliacao);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaAvaliacao);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ==================== BUSCAR POR ID ====================
    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoRisco> buscarPorId(@PathVariable UUID id) {
        try {
            AvaliacaoRisco avaliacao = avaliacaoRiscoService.buscarPorId(id);
            return ResponseEntity.ok(avaliacao);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==================== LISTAR AVALIAÇÕES POR CRIANÇA ====================
    @GetMapping("/crianca/{criancaId}")
    public ResponseEntity<List<AvaliacaoRisco>> listarPorCrianca(@PathVariable Long criancaId) {
        List<AvaliacaoRisco> avaliacoes = avaliacaoRiscoService.listarPorCrianca(criancaId);
        return ResponseEntity.ok(avaliacoes);
    }

    // ==================== ÚLTIMA AVALIAÇÃO POR ÁREA ====================
    @GetMapping("/crianca/{criancaId}/ultima-avaliacao")
    public ResponseEntity<Map<String, AvaliacaoRisco>> obterUltimasAvaliacoes(@PathVariable Long criancaId) {
        Map<String, AvaliacaoRisco> avaliacoes = avaliacaoRiscoService.obterUltimasAvaliacoesPorArea(criancaId);
        return ResponseEntity.ok(avaliacoes);
    }

    // ==================== AVALIAR RISCO AUTOMATICAMENTE [RF017] ====================
    @PostMapping("/crianca/{criancaId}/avaliar-automatico")
    public ResponseEntity<List<AvaliacaoRisco>> avaliarRiscoAutomatico(@PathVariable Long criancaId) {
        try {
            List<AvaliacaoRisco> avaliacoes = avaliacaoRiscoService.avaliarRiscoAutomatico(criancaId);
            return ResponseEntity.status(HttpStatus.CREATED).body(avaliacoes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ==================== RECOMENDAÇÕES DE AÇÕES PREVENTIVAS [RF018] ====================
    @GetMapping("/crianca/{criancaId}/recomendacoes")
    public ResponseEntity<List<String>> obterRecomendacoes(@PathVariable Long criancaId) {
        List<String> recomendacoes = avaliacaoRiscoService.obterRecomendacoes(criancaId);
        return ResponseEntity.ok(recomendacoes);
    }

    // ==================== VERIFICAR SE PRECISA BUSCAR PROFISSIONAL [RF019] ====================
    @GetMapping("/crianca/{criancaId}/precisa-profissional")
    public ResponseEntity<Map<String, Object>> verificarNecessidadeProfissional(@PathVariable Long criancaId) {
        Map<String, Object> resultado = avaliacaoRiscoService.verificarNecessidadeProfissional(criancaId);
        return ResponseEntity.ok(resultado);
    }

    // ==================== ALERTAS PENDENTES ====================
    @GetMapping("/crianca/{criancaId}/alertas-pendentes")
    public ResponseEntity<List<AvaliacaoRisco>> listarAlertasPendentes(@PathVariable Long criancaId) {
        List<AvaliacaoRisco> avaliacoes = avaliacaoRiscoService.listarAlertasPendentes(criancaId);
        return ResponseEntity.ok(avaliacoes);
    }

    // ==================== ENVIAR ALERTA [RF019] ====================
    @PostMapping("/{id}/enviar-alerta")
    public ResponseEntity<AvaliacaoRisco> enviarAlerta(@PathVariable UUID id) {
        try {
            AvaliacaoRisco avaliacao = avaliacaoRiscoService.enviarAlerta(id);
            return ResponseEntity.ok(avaliacao);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ==================== ATUALIZAR AVALIAÇÃO ====================
    @PutMapping("/{id}")
    public ResponseEntity<AvaliacaoRisco> atualizarAvaliacao(
            @PathVariable UUID id,
            @Valid @RequestBody AvaliacaoRisco avaliacaoAtualizada) {
        try {
            AvaliacaoRisco avaliacao = avaliacaoRiscoService.atualizarAvaliacao(id, avaliacaoAtualizada);
            return ResponseEntity.ok(avaliacao);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ==================== DELETAR AVALIAÇÃO ====================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAvaliacao(@PathVariable UUID id) {
        try {
            avaliacaoRiscoService.deletarAvaliacao(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ==================== DASHBOARD DE RISCOS ====================
    @GetMapping("/crianca/{criancaId}/dashboard")
    public ResponseEntity<Map<String, Object>> obterDashboardRiscos(@PathVariable Long criancaId) {
        Map<String, Object> dashboard = avaliacaoRiscoService.obterDashboardRiscos(criancaId);
        return ResponseEntity.ok(dashboard);
    }
}
