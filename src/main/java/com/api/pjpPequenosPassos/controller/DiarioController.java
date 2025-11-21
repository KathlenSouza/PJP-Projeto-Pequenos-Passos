package com.api.pjpPequenosPassos.controller;

import com.api.pjpPequenosPassos.model.Diario;
import com.api.pjpPequenosPassos.service.DiarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/diario")
@CrossOrigin(origins = "*")
public class DiarioController {

    @Autowired
    private DiarioService diarioService;

    // 🔹 LISTAR TODOS
    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            List<Diario> diarios = diarioService.listarTodos();
            return ResponseEntity.ok(diarios);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro ao listar registros: " + e.getMessage()));
        }
    }


    // 🔹 CRIAR NOVO REGISTRO
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Diario diario) {
        try {
            Diario novo = diarioService.criarRegistro(diario);
            Map<String, Object> resposta = new HashMap<>();
            resposta.put("mensagem", "Registro criado com sucesso!");
            resposta.put("registro", novo);
            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", e.getMessage()));
        }
    }


    // 🔹 BUSCAR POR DATA
    @GetMapping("/data/{data}")
    public ResponseEntity<?> buscarPorData(@PathVariable String data) {
        Map<String, Object> resposta = new HashMap<>();
        try {
            LocalDate dataConvertida = LocalDate.parse(data);
            List<Diario> lista = diarioService.buscarPorData(dataConvertida);
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            resposta.put("erro", "Data inválida ou erro ao buscar registros: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
        }
    }

    // 🔹 ATUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Diario diario) {
        Map<String, Object> resposta = new HashMap<>();
        try {
            Diario atualizado = diarioService.atualizarRegistro(id, diario);
            return ResponseEntity.ok(atualizado);
        } catch (Exception e) {
            resposta.put("erro", "Erro ao atualizar: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
        }
    }

    // 🔹 DESATIVAR (soft delete)
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<?> desativar(@PathVariable Long id) {
        Map<String, Object> resposta = new HashMap<>();
        try {
            diarioService.desativarRegistro(id);
            resposta.put("mensagem", "Registro desativado com sucesso!");
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            resposta.put("erro", "Erro ao desativar: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
        }
    }

    // 🔹 EXCLUIR (delete real)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        Map<String, Object> resposta = new HashMap<>();
        try {
            diarioService.deletarRegistro(id);
            resposta.put("mensagem", "Registro excluído com sucesso!");
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            resposta.put("erro", "Erro ao excluir: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
        }
    }
}

