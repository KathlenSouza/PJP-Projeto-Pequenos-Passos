package com.api.pjpPequenosPassos.controller;

import com.api.pjpPequenosPassos.dto.HistoricoAtividadeDTO;
import com.api.pjpPequenosPassos.mapper.HistoricoAtividadeMapper;
import com.api.pjpPequenosPassos.repository.HistoricoAtividadeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/historico")
@CrossOrigin(origins = "*")
public class HistoricoAtividadeController {

    private final HistoricoAtividadeRepository historicoRepo;

    public HistoricoAtividadeController(HistoricoAtividadeRepository historicoRepo) {
        this.historicoRepo = historicoRepo;
    }

    // LISTAR TUDO
    @GetMapping
    public ResponseEntity<List<HistoricoAtividadeDTO>> listarTudo() {
        var lista = historicoRepo.findAll();
        return ResponseEntity.ok(HistoricoAtividadeMapper.toDTOList(lista));
    }

    // LISTAR POR CRIANÇA
    @GetMapping("/crianca/{id}")
    public ResponseEntity<List<HistoricoAtividadeDTO>> listarPorCrianca(@PathVariable Long id) {
        var lista = historicoRepo.findByCriancaId(id);
        return ResponseEntity.ok(HistoricoAtividadeMapper.toDTOList(lista));
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable UUID id) {
        return historicoRepo.findById(id)
                .map(HistoricoAtividadeMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // EXCLUIR
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable UUID id) {
        if (!historicoRepo.existsById(id))
            return ResponseEntity.notFound().build();

        historicoRepo.deleteById(id);
        return ResponseEntity.ok(Map.of("mensagem", "Registro excluído com sucesso!"));
    }
}
