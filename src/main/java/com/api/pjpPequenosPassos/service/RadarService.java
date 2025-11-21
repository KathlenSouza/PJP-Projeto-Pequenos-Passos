	package com.api.pjpPequenosPassos.service;

	import com.api.pjpPequenosPassos.model.Tarefa;
	import com.api.pjpPequenosPassos.model.HistoricoAtividade;
	import com.api.pjpPequenosPassos.repository.HistoricoAtividadeRepository;
	import org.springframework.stereotype.Service;

	import java.util.*;
	import java.util.stream.Collectors;

	@Service
	public class RadarService {

	    private final HistoricoAtividadeRepository historicoRepo;
	    private final OpenAIService openAIService;

	    public RadarService(HistoricoAtividadeRepository historicoRepo, OpenAIService openAIService) {
	        this.historicoRepo = historicoRepo;
	        this.openAIService = openAIService;
	    }

	    // ============================================================
	    // 📌 GERAR RADAR (sem IA)
	    // ============================================================
	    public Map<String, Object> gerarRadar(Long criancaId) {

	        Map<String, Double> progressoPorArea = calcularProgresso(criancaId);

	        Map<String, Object> resposta = new LinkedHashMap<>();
	        resposta.put("rotulos", new ArrayList<>(progressoPorArea.keySet()));
	        resposta.put("valores", new ArrayList<>(progressoPorArea.values()));

	        // IA NÃO É USADA AQUI
	        resposta.put("recomendacoes", new ArrayList<>());

	        return resposta;
	    }


	    // ============================================================
	    // 📌 GERAR ANÁLISE IA (somente quando o usuário clicar)
	    // ============================================================
	    public List<String> gerarAnaliseIA(Long criancaId) {

	        Map<String, Double> progressoPorArea = calcularProgresso(criancaId);

	        String prompt = criarPromptParaIA(progressoPorArea);

	        String respostaIA = openAIService.generateText(prompt);

	        // Divide em linhas
	        return Arrays.asList(respostaIA.split("\\n"));
	    }


	    // ============================================================
	    // 📌 CÁLCULO REAL DO PROGRESSO
	    // ============================================================
	    private Map<String, Double> calcularProgresso(Long criancaId) {

	        List<Tarefa.AreaDesenvolvimento> areas = Arrays.asList(
	                Tarefa.AreaDesenvolvimento.MOTOR_GROSSO,
	                Tarefa.AreaDesenvolvimento.MOTOR_FINO,
	                Tarefa.AreaDesenvolvimento.LINGUAGEM,
	                Tarefa.AreaDesenvolvimento.COGNITIVO,
	                Tarefa.AreaDesenvolvimento.SOCIAL
	        );

	        Map<String, Double> progressoPorArea = new LinkedHashMap<>();

	        for (Tarefa.AreaDesenvolvimento area : areas) {

	            // Busca histórico concluído por área
	        	List<HistoricoAtividade> historicos = 
	        	        historicoRepo.findByCriancaIdOrderByDataConclusaoDesc(criancaId)
	        	        .stream()
	        	        .filter(h -> h.getTarefa() != null && h.getTarefa().getAreaDesenvolvimento() == area)
	        	        .toList();

	                        

	            // Exemplo simples: 10 tarefas = 100%
	            double progresso = historicos.isEmpty()
	                    ? 0.0
	                    : Math.min((historicos.size() / 10.0) * 100, 100.0);

	            progressoPorArea.put(formatarArea(area), progresso);
	        }

	        return progressoPorArea;
	    }


	    // ============================================================
	    // 📌 PROMPT PARA A IA (somente quando clicado)
	    // ============================================================
	    private String criarPromptParaIA(Map<String, Double> progresso) {
	        StringBuilder sb = new StringBuilder(
	                "Gere observações curtas, positivas e encorajadoras sobre o progresso da criança em cada área.\n"
	                        + "Use linguagem acessível para pais e educadores.\n"
	                        + "Uma frase por área.\n\n"
	        );

	        for (Map.Entry<String, Double> entry : progresso.entrySet()) {
	            sb.append(entry.getKey()).append(": ").append(entry.getValue().intValue()).append("%\n");
	        }

	        sb.append("\nResponda apenas com texto simples, uma frase por linha.");

	        return sb.toString();
	    }


	    // ============================================================
	    // 📌 FORMATAR NOMES DAS ÁREAS
	    // ============================================================
	    private String formatarArea(Tarefa.AreaDesenvolvimento area) {
	        switch (area) {
	            case MOTOR_GROSSO: return "Motor Grosso";
	            case MOTOR_FINO: return "Motor Fino";
	            case LINGUAGEM: return "Linguagem";
	            case COGNITIVO: return "Cognitivo";
	            case SOCIAL: return "Socioemocional";
	            default: return area.name();
	        }
	    }
	}

