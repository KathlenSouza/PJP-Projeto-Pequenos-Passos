package com.api.pjpPequenosPassos.repository;

import com.api.pjpPequenosPassos.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    List<Agenda> findByDataAgendada(LocalDate data);
    List<Agenda> findAllByOrderByDataAgendadaAsc();
}
