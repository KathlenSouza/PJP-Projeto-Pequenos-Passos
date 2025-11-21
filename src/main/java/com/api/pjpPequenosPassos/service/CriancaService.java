package com.api.pjpPequenosPassos.service;

import com.api.pjpPequenosPassos.model.Crianca;
import com.api.pjpPequenosPassos.model.Usuario;
import com.api.pjpPequenosPassos.repository.CriancaRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CriancaService {

    private final CriancaRepository criancaRepository;
    private final UsuarioService usuarioService;

    @Autowired
    public CriancaService(CriancaRepository criancaRepository, UsuarioService usuarioService) {
        this.criancaRepository = criancaRepository;
        this.usuarioService = usuarioService;
    }
    // ==================== CRIAR CRIANÇA ====================
    @Transactional
    public Crianca criarCrianca(Long usuarioId, Crianca crianca) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        
        // Validar faixa etária (4 a 6 anos)
        int idade = crianca.getIdade();
        if (idade < 4 || idade > 6) {
            throw new RuntimeException("A criança deve ter entre 4 e 6 anos");
        }

        crianca.setUsuario(usuario);
        return criancaRepository.save(crianca);
    }

    // ==================== BUSCAR POR ID ====================
    @Transactional(readOnly = true)
    public Crianca buscarPorId(Long id) {
        return criancaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Criança não encontrada"));
    }


    // ==================== ATUALIZAR CRIANÇA [RF002] ====================
    @Transactional
    public Crianca atualizarCrianca(Long id, Crianca criancaAtualizada) {
        Crianca crianca = buscarPorId(id);

        crianca.setNome(criancaAtualizada.getNome());
        crianca.setDataNascimento(criancaAtualizada.getDataNascimento());
        crianca.setGenero(criancaAtualizada.getGenero());
     
        return criancaRepository.save(crianca);
    }

  
 // ==================== BUSCAR POR FAIXA ETÁRIA ====================
    @Transactional(readOnly = true)
    public List<Crianca> buscarPorFaixaEtaria(Long usuarioId, int idadeMin, int idadeMax) {
        LocalDate hoje = LocalDate.now();
        LocalDate dataMax = hoje.minusYears(idadeMin);
        LocalDate dataMin = hoje.minusYears(idadeMax + 1).plusDays(1);

        return criancaRepository.findByUsuarioIdAndDataNascimentoBetween(usuarioId, dataMin, dataMax);
    }



    // ==================== VALIDAR PROPRIEDADE ====================
    public boolean validarProprietario(Long criancaId, Long usuarioId) {
        Crianca crianca = buscarPorId(criancaId);
        return crianca.getUsuario().getId().equals(usuarioId);
        }    
}