package com.api.pjpPequenosPassos.service;

import com.api.pjpPequenosPassos.model.AvaliacaoRisco;
import com.api.pjpPequenosPassos.model.Crianca;
import com.api.pjpPequenosPassos.repository.AvaliacaoRiscoRepository;
import com.api.pjpPequenosPassos.repository.CriancaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AvaliacaoRiscoService {

    @Autowired
    private AvaliacaoRiscoRepository avaliacaoRiscoRepository;

    @Autowired
    private CriancaRepository criancaRepository;

    // ==================== CRIAR AVALIAÇÃO [RF017] ====================
    @Transactional
    public AvaliacaoRisco criarAvaliacao(AvaliacaoRisco avaliacao) {
        if (avaliacao.getCrianca() == null || avaliacao.getCrianca().getId() == null) {
            throw new IllegalArgumentException("A avaliação deve estar associada a uma criança válida.");
        }

        Crianca crianca = criancaRepository.findById(avaliacao.getCrianca().getId())
                .orElseThrow(() -> new RuntimeException("Criança não encontrada"));

        avaliacao.setCrianca(crianca);
        return avaliacaoRiscoRepository.save(avaliacao);
    }

    // ==================== BUSCAR POR ID ====================
    public AvaliacaoRisco buscarPorId(UUID id) {
        return avaliacaoRiscoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));
    }

    // ==================== LISTAR AVALIAÇÕES POR CRIANÇA ====================
    public List<AvaliacaoRisco> listarPorCrianca(Long criancaId) {
        return avaliacaoRiscoRepository.findByCriancaId(criancaId);
    }

    // ==================== ÚLTIMA AVALIAÇÃO POR ÁREA ====================
    public Map<String, AvaliacaoRisco> obterUltimasAvaliacoesPorArea(Long criancaId) {
        Map<String, AvaliacaoRisco> ultimas = new HashMap<>();
        List<AvaliacaoRisco> avaliacoes = avaliacaoRiscoRepository.findByCriancaIdOrderByIdDesc(criancaId);

        for (AvaliacaoRisco avaliacao : avaliacoes) {
            if (!ultimas.containsKey(avaliacao.getTipoRisco())) {
                ultimas.put(avaliacao.getTipoRisco(), avaliacao);
            }
        }

        return ultimas;
    }

    // ==================== AVALIAR RISCO AUTOMATICAMENTE [RF017] ====================
    @Transactional
    public List<AvaliacaoRisco> avaliarRiscoAutomatico(Long criancaId) {
        Crianca crianca = criancaRepository.findById(criancaId)
                .orElseThrow(() -> new RuntimeException("Criança não encontrada"));

        String[] tipos = {"Saúde", "Comportamento", "Nutrição"};
        List<AvaliacaoRisco> geradas = new ArrayList<>();

        for (String tipo : tipos) {
            AvaliacaoRisco nova = new AvaliacaoRisco();
            nova.setTipoRisco(tipo);
            nova.setObservacao("Avaliação automática de " + tipo);
            nova.setCrianca(crianca);

            geradas.add(avaliacaoRiscoRepository.save(nova));
        }

        return geradas;
    }

    // ==================== RECOMENDAÇÕES DE AÇÕES PREVENTIVAS [RF018] ====================
    public List<String> obterRecomendacoes(Long criancaId) {
        List<AvaliacaoRisco> avaliacoes = listarPorCrianca(criancaId);
        List<String> recomendacoes = new ArrayList<>();

        for (AvaliacaoRisco avaliacao : avaliacoes) {
            switch (avaliacao.getTipoRisco().toLowerCase()) {
                case "saúde" -> recomendacoes.add("Verificar consultas médicas de rotina.");
                case "comportamento" -> recomendacoes.add("Observar interações sociais e rotinas.");
                case "nutrição" -> recomendacoes.add("Manter alimentação equilibrada e acompanhamento nutricional.");
                default -> recomendacoes.add("Manter acompanhamento geral.");
            }
        }

        return recomendacoes;
    }

    // ==================== VERIFICAR SE PRECISA BUSCAR PROFISSIONAL [RF019] ====================
    public Map<String, Object> verificarNecessidadeProfissional(Long criancaId) {
        Map<String, Object> resultado = new HashMap<>();
        List<AvaliacaoRisco> avaliacoes = listarPorCrianca(criancaId);

        boolean precisa = !avaliacoes.isEmpty(); // lógica simples
        resultado.put("precisaProfissional", precisa);
        resultado.put("quantidadeAvaliacoes", avaliacoes.size());
        resultado.put("tipos", avaliacoes.stream().map(AvaliacaoRisco::getTipoRisco).toList());

        return resultado;
    }

    // ==================== ALERTAS PENDENTES ====================
    public List<AvaliacaoRisco> listarAlertasPendentes(Long criancaId) {
        return avaliacaoRiscoRepository.findByCriancaId(criancaId);
    }

    // ==================== ENVIAR ALERTA [RF019] ====================
    @Transactional
    public AvaliacaoRisco enviarAlerta(UUID id) {
        AvaliacaoRisco avaliacao = buscarPorId(id);
        // Aqui poderia ser enviada uma notificação, e-mail, etc.
        return avaliacao;
    }

    // ==================== ATUALIZAR AVALIAÇÃO ====================
    @Transactional
    public AvaliacaoRisco atualizarAvaliacao(UUID id, AvaliacaoRisco atualizada) {
        AvaliacaoRisco existente = buscarPorId(id);
        existente.setTipoRisco(atualizada.getTipoRisco());
        existente.setObservacao(atualizada.getObservacao());
        return avaliacaoRiscoRepository.save(existente);
    }

    // ==================== DELETAR AVALIAÇÃO ====================
    @Transactional
    public void deletarAvaliacao(UUID id) {
        avaliacaoRiscoRepository.deleteById(id);
    }

    // ==================== DASHBOARD DE RISCOS ====================
    public Map<String, Object> obterDashboardRiscos(Long criancaId) {
        List<AvaliacaoRisco> avaliacoes = listarPorCrianca(criancaId);
        Map<String, Long> contagemPorTipo = new HashMap<>();

        avaliacoes.stream()
                .map(AvaliacaoRisco::getTipoRisco)
                .forEach(tipo -> contagemPorTipo.put(tipo,
                        contagemPorTipo.getOrDefault(tipo, 0L) + 1));

        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("totalAvaliacoes", avaliacoes.size());
        dashboard.put("porTipo", contagemPorTipo);
        dashboard.put("ultimaAtualizacao", new Date());
        return dashboard;
    }
}
