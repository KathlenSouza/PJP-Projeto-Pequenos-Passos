package com.api.pjpPequenosPassos.repository;

import com.api.pjpPequenosPassos.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, UUID> {

    // Profissionais indicados por pais
    List<Profissional> findByIndicadoPorPaisTrue();

    // Busca por cidade ou área (para filtros)
    List<Profissional> findByCidadeContainingIgnoreCaseOrAreaContainingIgnoreCase(String cidade, String area);
    
    //Listar os ultimos profissionais top5
    List<Profissional> findTop5ByOrderByIdDesc();
    
    // Buscar por avaliação mínima (estrelas)
    List<Profissional> findByAvaliacaoGreaterThanEqual(Integer nota);
    
    //Buscar por nome
    List<Profissional> findByNomeContainingIgnoreCase(String nome);



}