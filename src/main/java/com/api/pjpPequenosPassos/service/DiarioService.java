package com.api.pjpPequenosPassos.service;

import com.api.pjpPequenosPassos.dto.DiarioRequest;
import com.api.pjpPequenosPassos.model.Crianca;
import com.api.pjpPequenosPassos.model.Diario;
import com.api.pjpPequenosPassos.repository.CriancaRepository;
import com.api.pjpPequenosPassos.repository.DiarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class DiarioService {
	@Autowired
	private CriancaRepository criancaRepository;

    private final DiarioRepository diarioRepository;

    // Construtor manual para injeção de dependência
    public DiarioService(DiarioRepository diarioRepository) {
        this.diarioRepository = diarioRepository;
    }
    public List<Diario> buscarPorCrianca(Long criancaId) {
        return diarioRepository.findByCriancaId(criancaId);
    }

   

    // ==================== CRIAR REGISTRO ====================
 // ==================== CRIAR REGISTRO ====================
    public Diario criarRegistro(DiarioRequest dto) {

        Crianca crianca = criancaRepository.findById(dto.getCriancaId())
                .orElseThrow(() -> new RuntimeException("Criança não encontrada"));

        Diario diario = new Diario();
        diario.setCrianca(crianca);
        diario.setDescricao(dto.getDescricao());
        diario.setEmocao(dto.getEmocao());
        diario.setDataRegistro(LocalDate.now());
        diario.setAtivo(true);

        
        return diarioRepository.save(diario);
    }


    // ==================== LISTAR TODOS ====================
    @Transactional(readOnly = true)
    public List<Diario> listarTodos() {
        return diarioRepository.findByAtivoTrueOrderByDataRegistroDesc();
    }

    // ==================== BUSCAR POR ID ====================
    @Transactional(readOnly = true)
    public Diario buscarPorId(Long id) {
        return diarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro de diário não encontrado."));
    }

    // ==================== BUSCAR POR DATA ====================
  
    @Transactional(readOnly = true)
    public List<Diario> buscarPorData(LocalDate data) {
        return diarioRepository.findByDataRegistro(data);
    }

    @Transactional(readOnly = true)
    public List<Diario> buscarEntreDatas(LocalDate inicio, LocalDate fim) {
        return diarioRepository.findByDataRegistroBetween(inicio, fim);
    }

    // ==================== ATUALIZAR REGISTRO ====================
    @Transactional
    public Diario atualizarRegistro(Long id, Diario atualizado) {
        Diario existente = buscarPorId(id);

        existente.setEmocao(atualizado.getEmocao());
        existente.setDescricao(atualizado.getDescricao());
        existente.setFotos(atualizado.getFotos());

        return diarioRepository.save(existente);
    }

    // ==================== DESATIVAR REGISTRO ====================
    @Transactional
    public void desativarRegistro(Long id) {
        Diario diario = buscarPorId(id);
        diario.setAtivo(false);
        diarioRepository.save(diario);
    }

    // ==================== DELETAR DEFINITIVAMENTE ====================
    @Transactional
    public void deletarRegistro(Long id) {
        Diario diario = buscarPorId(id);
        diarioRepository.delete(diario);
    }
    
 
}


