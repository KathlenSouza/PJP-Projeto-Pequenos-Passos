package com.api.pjpPequenosPassos.controller;

import com.api.pjpPequenosPassos.dto.TarefaDTO;
import com.api.pjpPequenosPassos.mapper.TarefaMapper;
import com.api.pjpPequenosPassos.model.Crianca;
import com.api.pjpPequenosPassos.model.HistoricoAtividade;
import com.api.pjpPequenosPassos.model.Tarefa;
import com.api.pjpPequenosPassos.repository.HistoricoAtividadeRepository;
import com.api.pjpPequenosPassos.repository.TarefaRepository;
import com.api.pjpPequenosPassos.service.SugerirTarefasService;
import com.api.pjpPequenosPassos.service.TarefaService;

import jakarta.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tarefas")
@CrossOrigin(origins = "*")
public class TarefaController {

    private final TarefaService tarefaService;
    private final SugerirTarefasService sugerirTarefasService;
    private final TarefaRepository tarefaRepo;
    private final HistoricoAtividadeRepository historicoRepo;

  
    public TarefaController(TarefaService tarefaService, SugerirTarefasService sugerirTarefasService,TarefaRepository tarefaRepo,
            HistoricoAtividadeRepository historicoRepo) {
        this.tarefaService = tarefaService;
        this.sugerirTarefasService = sugerirTarefasService;
        this.tarefaRepo = tarefaRepo;
        this.historicoRepo = historicoRepo;
    }
    
    

    // ==================== LISTAR TODAS ====================
    @GetMapping
    public ResponseEntity<List<TarefaDTO>> listar() {
        var tarefas = tarefaService.listarTodas();
        return ResponseEntity.ok(TarefaMapper.toDTOList(tarefas));
    }
    @GetMapping("/todas")
    public ResponseEntity<List<TarefaDTO>> listarTodas() {
        var tarefas = tarefaService.listarTodas();
        return ResponseEntity.ok(TarefaMapper.toDTOList(tarefas));
    }

    // ==================== BUSCAR POR ID ====================
    @GetMapping("/{id}")
    public ResponseEntity<TarefaDTO> buscarPorId(@PathVariable Long id) {
        try {
            var tarefa = tarefaService.buscarPorId(id);
            return ResponseEntity.ok(TarefaMapper.toDTO(tarefa));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==================== CRIAR ====================
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Tarefa tarefa) {
        try {
            var salva = tarefaService.criarTarefa(tarefa);
            return ResponseEntity.status(201).body(TarefaMapper.toDTO(salva));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    // ==================== ATUALIZAR ====================
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Tarefa tarefaAtualizada) {
        try {
            var atualizada = tarefaService.atualizarTarefa(id, tarefaAtualizada);
            return ResponseEntity.ok(TarefaMapper.toDTO(atualizada));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", "Erro ao atualizar tarefa."));
        }
    }

    // ==================== DELETAR ====================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            tarefaService.deletarTarefa(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==================== DESATIVAR (CONCLUIR) ====================
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Tarefa> desativar(@PathVariable Long id) {

        Tarefa tarefa = tarefaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        tarefa.setAtivo(false);
        tarefaRepo.save(tarefa);

        // --- CRIAR REGISTRO NO HISTÓRICO ---
        HistoricoAtividade hist = new HistoricoAtividade();
        hist.setTarefa(tarefa);
        hist.setCriancaId(1L); // <<< depois você muda para o ID correto
        hist.setDataConclusao(LocalDateTime.now());
        historicoRepo.save(hist);
        // -----------------------------------

        return ResponseEntity.ok(tarefa);
    }


    // ==================== POPULAR TAREFAS BASE ====================
    @PostConstruct
    public void init() {
        tarefaService.popularTarefasBase();
    }

    // ==================== SUGESTÕES (BANCO DE DADOS) ====================
    @GetMapping("/sugerir")
    public ResponseEntity<List<TarefaDTO>> sugerir(
            @RequestParam int idade,
            @RequestParam(required = false) String area
    ) {
        Tarefa.AreaDesenvolvimento areaEnum = null;
        try {
            if (area != null && !area.isBlank()) {
                areaEnum = Tarefa.AreaDesenvolvimento.valueOf(area.toUpperCase().replace(' ', '_'));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(List.of());
        }

        var sugestoes = tarefaService.obterSugestoes(idade, areaEnum);
        return ResponseEntity.ok(TarefaMapper.toDTOList(sugestoes));
    }

    // ==================== SUGESTÕES (INTELIGÊNCIA ARTIFICIAL) ====================
    @PostMapping("/sugerir-ia")
    public ResponseEntity<Map<String, Object>> gerarSugestoesIA(@RequestBody Crianca crianca) {

        List<Tarefa> tarefasGeradas = sugerirTarefasService.gerarSugestoesIA(crianca);

        // SALVAR NO BANCO
        List<Tarefa> salvas = tarefasGeradas.stream()
                .map(tarefaService::criarTarefa)
                .toList();

        // Transformar em DTO para o front
        var dtos = TarefaMapper.toDTOList(salvas);

        Map<String, Object> response = new HashMap<>();
        response.put("sugestoes", dtos);

        return ResponseEntity.ok(response);
    }

    // ==================== POPULAR MANUAL ====================
    @PostMapping("/popular")
    public ResponseEntity<?> popular() {
        tarefaService.popularTarefasBase();
        return ResponseEntity.ok(Map.of("mensagem", "Base de tarefas populada (se estava vazia)."));
    }
 // ==================== LISTAR HISTÓRICO POR CRIANÇA ====================
    @GetMapping("/historico")
    public ResponseEntity<?> listarHistorico(@RequestParam Long criancaId) {
        try {
            List<HistoricoAtividade> lista = historicoRepo.findByCriancaId(criancaId);

            return ResponseEntity.ok(
                    lista.stream().map(hist -> {
                        Map<String, Object> dto = new HashMap<>();
                        dto.put("id", hist.getId());
                        dto.put("criancaId", hist.getCriancaId());
                        dto.put("dataConclusao", hist.getDataConclusao());
                        
                        if (hist.getTarefa() != null) {
                            dto.put("tarefa", hist.getTarefa().getTitulo());
                            dto.put("descricao", hist.getTarefa().getDescricao());
                            dto.put("areaDesenvolvimento", hist.getTarefa().getAreaDesenvolvimento());
                            dto.put("categoria", hist.getTarefa().getCategoria());
                        }


                        dto.put("status", "Concluída");

                        return dto;
                    }).toList()
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", "Erro ao buscar histórico."));
        }
    }
    
    @GetMapping("/crianca/{criancaId}/pendentes")
    public ResponseEntity<?> listarPendentes(@PathVariable Long criancaId) {
        try {
            var lista = tarefaService.listarPendentesPorCrianca(criancaId);
            return ResponseEntity.ok(TarefaMapper.toDTOList(lista));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("erro", e.getMessage()));
        }
    }
   
    @GetMapping("/pendentes/crianca/{criancaId}")
    public ResponseEntity<List<TarefaDTO>> listarPendentesPorCrianca(@PathVariable Long criancaId) {
        List<Tarefa> pendentes = tarefaService.listarPendentesPorCrianca(criancaId);
        return ResponseEntity.ok(TarefaMapper.toDTOList(pendentes));
    }
}
