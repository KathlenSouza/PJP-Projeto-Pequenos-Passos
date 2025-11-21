package com.api.pjpPequenosPassos.controller;

import com.api.pjpPequenosPassos.model.Crianca;
import com.api.pjpPequenosPassos.model.Usuario;
import com.api.pjpPequenosPassos.repository.CriancaRepository;
import com.api.pjpPequenosPassos.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/criancas")
@CrossOrigin(origins = "*")
public class CriancaController {

    private final CriancaRepository criancaRepository;
    private final UsuarioRepository usuarioRepository;

    public CriancaController(CriancaRepository criancaRepository,
                             UsuarioRepository usuarioRepository) {
        this.criancaRepository = criancaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public ResponseEntity<List<Crianca>> listar() {
        return ResponseEntity.ok(criancaRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Crianca> obter(@PathVariable Long id) {
        return criancaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Crianca> criar(@Valid @RequestBody Crianca dto) {
        if (dto.getUsuario() == null || dto.getUsuario().getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Usuario u = usuarioRepository.findById(dto.getUsuario().getId()).orElse(null);
        if (u == null) return ResponseEntity.badRequest().build();

        dto.setId(null);          // garante insert
        dto.setUsuario(u);
        Crianca salva = criancaRepository.save(dto);
        return ResponseEntity.created(URI.create("/api/criancas/" + salva.getId())).body(salva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Crianca> atualizar(@PathVariable Long id,
                                             @Valid @RequestBody Crianca dados) {
        return criancaRepository.findById(id).map(existing -> {
            existing.setNome(dados.getNome());
            existing.setDataNascimento(dados.getDataNascimento());
            if (dados.getUsuario() != null && dados.getUsuario().getId() != null) {
                Usuario u = usuarioRepository.findById(dados.getUsuario().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
                existing.setUsuario(u);
            }
            return ResponseEntity.ok(criancaRepository.save(existing));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!criancaRepository.existsById(id)) return ResponseEntity.notFound().build();
        criancaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
