package com.api.pjpPequenosPassos.repository;

import com.api.pjpPequenosPassos.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CriancaRepository extends JpaRepository<Crianca, Long> {
    List<Crianca> findByUsuarioId(Long usuarioId);
   // List<Crianca> findByUsuarioIdAndAtivoTrue(Long usuarioId);
    

    @Query("SELECT c FROM Crianca c WHERE c.usuario.id = :usuarioId " +
    	       "AND c.dataNascimento BETWEEN :dataMin AND :dataMax")
    	List<Crianca> findByUsuarioIdAndDataNascimentoBetween(
    	    @Param("usuarioId") Long usuarioId,
    	    @Param("dataMin") LocalDate dataMin,
    	    @Param("dataMax") LocalDate dataMax
    	);

}
