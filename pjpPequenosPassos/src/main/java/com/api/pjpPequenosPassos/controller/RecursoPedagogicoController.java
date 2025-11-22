package com.api.pjpPequenosPassos.controller;

import com.api.pjpPequenosPassos.model.RecursoPedagogico;
import com.api.pjpPequenosPassos.repository.RecursoPedagogicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recursos")
@CrossOrigin("*")
public class RecursoPedagogicoController {

    @Autowired
    private RecursoPedagogicoRepository repository;

    @GetMapping
    public List<RecursoPedagogico> listar() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<RecursoPedagogico> criar(@RequestBody RecursoPedagogico r) {
        return ResponseEntity.ok(repository.save(r));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable UUID id) {
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
