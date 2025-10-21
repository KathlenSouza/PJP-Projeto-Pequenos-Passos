package com.api.pjpPequenosPassos.repository;

import com.api.pjpPequenosPassos.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
    List<Profissional> findByAtivoTrue();
    List<Profissional> findByCidadeAndEstadoAndAtivoTrue(String cidade, String estado);
    List<Profissional> findByEspecialidadeAndAtivoTrue(String especialidade);

    @Query("SELECT p FROM Profissional p WHERE p.ativo = true AND p.cidade = :cidade AND p.estado = :estado ORDER BY p.mediaAvaliacao DESC")
    List<Profissional> findByRegiaoOrderByAvaliacao(@Param("cidade") String cidade, @Param("estado") String estado);
}
