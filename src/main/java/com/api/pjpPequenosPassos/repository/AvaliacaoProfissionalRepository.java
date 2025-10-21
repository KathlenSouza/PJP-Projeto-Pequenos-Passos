package com.api.pjpPequenosPassos.repository;

import com.api.pjpPequenosPassos.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvaliacaoProfissionalRepository extends JpaRepository<AvaliacaoProfissional, Long> {
    List<AvaliacaoProfissional> findByProfissionalId(Long profissionalId);
    List<AvaliacaoProfissional> findByUsuarioId(Long usuarioId);
    Optional<AvaliacaoProfissional> findByProfissionalIdAndUsuarioId(Long profissionalId, Long usuarioId);

    @Query("SELECT AVG(a.nota) FROM AvaliacaoProfissional a WHERE a.profissional.id = :profissionalId")
    Double calcularMediaAvaliacoes(@Param("profissionalId") Long profissionalId);
}
