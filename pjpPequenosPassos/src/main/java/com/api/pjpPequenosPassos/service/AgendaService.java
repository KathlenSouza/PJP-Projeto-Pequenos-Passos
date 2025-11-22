package com.api.pjpPequenosPassos.service;

import com.api.pjpPequenosPassos.model.Agenda;
import com.api.pjpPequenosPassos.repository.AgendaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AgendaService {

    private final AgendaRepository agendaRepository;

    public AgendaService(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    // Criar evento
    @Transactional
    public Agenda criarEvento(Agenda agenda) {
        if (agenda.getTitulo() == null || agenda.getTitulo().isEmpty()) {
            throw new RuntimeException("O título do evento é obrigatório.");
        }
        if (agenda.getDataAgendada() == null) {
            agenda.setDataAgendada(LocalDate.now());
        }

        agenda.setStatus(Agenda.StatusAgenda.PENDENTE);
        return agendaRepository.save(agenda);
    }

    // Listar todos os eventos
    @Transactional(readOnly = true)
    public List<Agenda> listarTodos() {
        return agendaRepository.findAllByOrderByDataAgendadaAsc();
    }

    // Buscar eventos por data
    @Transactional(readOnly = true)
    public List<Agenda> buscarPorData(LocalDate data) {
        return agendaRepository.findByDataAgendada(data);
    }

    // Atualizar evento
    @Transactional
    public Agenda atualizarEvento(Long id, Agenda atualizado) {
        Agenda existente = agendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        existente.setTitulo(atualizado.getTitulo());
        existente.setDescricao(atualizado.getDescricao());
        existente.setNotas(atualizado.getNotas());
        existente.setDataAgendada(atualizado.getDataAgendada());
        existente.setStatus(atualizado.getStatus());

        return agendaRepository.save(existente);
    }

    // Excluir evento
    @Transactional
    public void deletarEvento(Long id) {
        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));
        agendaRepository.delete(agenda);
    }
}

