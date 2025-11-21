package com.api.pjpPequenosPassos.service;

import com.api.pjpPequenosPassos.model.Relatorio;
import com.api.pjpPequenosPassos.model.Usuario;
import com.api.pjpPequenosPassos.repository.RelatorioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class RelatorioService {

    private final RelatorioRepository relatorioRepository;

    public RelatorioService(RelatorioRepository relatorioRepository) {
        this.relatorioRepository = relatorioRepository;
    }

    // ==================== GERAR RELATÓRIO ====================
    @Transactional
    public Relatorio gerarRelatorio(String tipo, String conteudo, Usuario usuario) {
        Relatorio relatorio = new Relatorio();
        relatorio.setTipo(tipo);
        relatorio.setConteudo(conteudo);
        relatorio.setUsuario(usuario);
        relatorio.setDataGeracao(LocalDate.now());
        return relatorioRepository.save(relatorio);
    }

    // ==================== LISTAR TODOS ====================
    @Transactional(readOnly = true)
    public List<Relatorio> listarTodos() {
        return relatorioRepository.findAll();
    }

    // ==================== BUSCAR POR TIPO ====================
    @Transactional(readOnly = true)
    public List<Relatorio> buscarPorTipo(String tipo) {
        return relatorioRepository.findByTipo(tipo);
    }

    // ==================== BUSCAR POR USUÁRIO ====================
    @Transactional(readOnly = true)
    public List<Relatorio> buscarPorUsuario(Long usuarioId) {
        return relatorioRepository.findByUsuarioId(usuarioId);
    }

    // ==================== DELETAR RELATÓRIO ====================
    @Transactional
    public void deletar(UUID id) {
        relatorioRepository.deleteById(id);
    }
}
