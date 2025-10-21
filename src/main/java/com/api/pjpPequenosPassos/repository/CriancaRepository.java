package com.api.pjpPequenosPassos.repository;

import com.api.pjpPequenosPassos.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CriancaRepository extends JpaRepository<Crianca, Long> {
    List<Crianca> findByUsuarioId(Long usuarioId);
    List<Crianca> findByUsuarioIdAndAtivoTrue(Long usuarioId);

    @Query("SELECT c FROM Crianca c WHERE c.usuario.id = :usuarioId AND " +
           "YEAR(CURRENT_DATE) - YEAR(c.dataNascimento) BETWEEN :idadeMin AND :idadeMax")
    List<Crianca> findByUsuarioIdAndFaixaEtaria(
        @Param("usuarioId") Long usuarioId,
        @Param("idadeMin") int idadeMin,
        @Param("idadeMax") int idadeMax
    );
}
