package com.api.pjpPequenosPassos.controller;

import com.api.pjpPequenosPassos.dto.VacinaDTO;
import com.api.pjpPequenosPassos.mapper.VacinaMapper;
import com.api.pjpPequenosPassos.model.Vacina;
import com.api.pjpPequenosPassos.repository.VacinaRepository;
import com.api.pjpPequenosPassos.service.VacinaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vacinas")
@CrossOrigin(origins = "*")
public class VacinaController {
	@Autowired
    private VacinaRepository repo;

    private final VacinaService service;

    public VacinaController(VacinaService service) {
        this.service = service;
    }
    
    @GetMapping("/crianca/{criancaId}")
    public ResponseEntity<?> listarPorCrianca(@PathVariable Long criancaId) {
        return ResponseEntity.ok(repo.findByCriancaId(criancaId));
    }
    @PostMapping
    public ResponseEntity<VacinaDTO> criar(@RequestBody Vacina vacina) {
        var salva = service.salvar(vacina);
        return ResponseEntity.ok(VacinaMapper.toDTO(salva));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
