package com.api.pjpPequenosPassos.service;

import com.api.pjpPequenosPassos.model.Diario;
import com.api.pjpPequenosPassos.repository.DiarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class DiarioService {

    private final DiarioRepository diarioRepository;

    // Construtor manual para injeção de dependência
    public DiarioService(DiarioRepository diarioRepository) {
        this.diarioRepository = diarioRepository;
    }
   

    // ==================== CRIAR REGISTRO ====================
    @Transactional
    public Diario criarRegistro(Diario diario) {
        // Validações simples
        if (diario.getEmocao() == null || diario.getEmocao().isEmpty()) {
            throw new RuntimeException("A emoção é obrigatória.");
        }
        if (diario.getDataRegistro() == null) {
            diario.setDataRegistro(LocalDate.now());
        }

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


