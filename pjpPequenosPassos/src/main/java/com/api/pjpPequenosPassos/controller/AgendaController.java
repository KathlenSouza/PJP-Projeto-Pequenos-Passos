package com.api.pjpPequenosPassos.controller;

import com.api.pjpPequenosPassos.model.Agenda;
import com.api.pjpPequenosPassos.service.AgendaService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/agenda")
@CrossOrigin(origins = "*")
public class AgendaController {

    private final AgendaService agendaService;

    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @PostMapping
    public Agenda criar(@RequestBody Agenda agenda) {
        return agendaService.criarEvento(agenda);
    }

    @GetMapping
    public List<Agenda> listar() {
        return agendaService.listarTodos();
    }

    @GetMapping("/data/{data}")
    public List<Agenda> buscarPorData(@PathVariable String data) {
        LocalDate date = LocalDate.parse(data);
        return agendaService.buscarPorData(date);
    }

    @PutMapping("/{id}")
    public Agenda atualizar(@PathVariable Long id, @RequestBody Agenda atualizado) {
        return agendaService.atualizarEvento(id, atualizado);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        agendaService.deletarEvento(id);
    }
}
