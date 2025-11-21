package com.api.pjpPequenosPassos.service;

import com.api.pjpPequenosPassos.model.HistoricoAtividade;
import com.api.pjpPequenosPassos.repository.HistoricoAtividadeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class HistoricoAtividadeService {

    private static final Logger log = LoggerFactory.getLogger(HistoricoAtividadeService.class);

    @Autowired
    private HistoricoAtividadeRepository historicoRepo;

    // ==================== BUSCAR POR ID ====================
    public HistoricoAtividade buscarPorId(UUID id) {
        log.info("Buscando histórico pelo ID: {}", id);
        return historicoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Histórico não encontrado para o ID: " + id));
    }

    // ==================== LISTAR TODOS ====================
    public List<HistoricoAtividade> listarTodos() {
        return historicoRepo.findAll();
    }

    // ==================== FILTRAR POR PERÍODO ====================
    public List<HistoricoAtividade> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return historicoRepo.findByDataConclusaoBetween(inicio, fim);
    }

    // ==================== SALVAR ====================
    public HistoricoAtividade salvar(HistoricoAtividade historico) {
        return historicoRepo.save(historico);
    }

    // ==================== DELETAR ====================
    public void deletar(UUID id) {
        historicoRepo.deleteById(id);
    }
}


