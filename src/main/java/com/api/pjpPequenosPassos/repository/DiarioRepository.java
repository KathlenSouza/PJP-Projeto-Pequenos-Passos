package com.api.pjpPequenosPassos.repository;

import com.api.pjpPequenosPassos.model.Diario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiarioRepository extends JpaRepository<Diario, Long> {

    List<Diario> findByAtivoTrueOrderByDataRegistroDesc();

    List<Diario> findByDataRegistro(LocalDate data);
}
