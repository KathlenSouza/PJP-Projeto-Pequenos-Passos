package com.api.pjpPequenosPassos.service;

import com.api.pjpPequenosPassos.model.Crianca;
import com.api.pjpPequenosPassos.model.Tarefa;

import org.springframework.stereotype.Service;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SugerirTarefasService {

    private final OpenAIService openAIService;

    @Autowired
    public SugerirTarefasService(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    public List<Tarefa> gerarSugestoesIA(Crianca crianca) {

    	String prompt = String.format(
    	        "Gere EXATAMENTE 3 atividades educativas em formato JSON PURO. " +
    	        "NÃO ESCREVA NADA ALÉM DO JSON.\n\n" +

    	        "REGRAS OBRIGATÓRIAS:\n" +
    	        "1) \"faixaEtariaMin\" deve ser sempre 4.\n" +
    	        "2) \"faixaEtariaMax\" deve ser sempre 6.\n" +
    	        "3) \"areaDesenvolvimento\" deve ser exatamente um destes valores (SEM ACENTOS, SEM VARIAÇÕES):\n" +
    	        "   - MOTOR_GROSSO\n" +
    	        "   - MOTOR_FINO\n" +
    	        "   - LINGUAGEM\n" +
    	        "   - COGNITIVO\n" +
    	        "   - SOCIAL\n" +
    	        "   NÃO use LINGUÍSTICO. NÃO crie novos termos.\n\n" +

    	        "FORMATO EXATO DO JSON DE RETORNO:\n" +
    	        "[{\n" +
    	        "  \"titulo\": \"\",\n" +
    	        "  \"descricao\": \"\",\n" +
    	        "  \"areaDesenvolvimento\": \"MOTOR_GROSSO\",\n" +
    	        "  \"categoria\": \"\",\n" +
    	        "  \"faixaEtariaMin\": 4,\n" +
    	        "  \"faixaEtariaMax\": 6,\n" +
    	        "  \"nivelDificuldade\": \"Fácil\",\n" +
    	        "  \"duracaoEstimadaMinutos\": 10,\n" +
    	        "  \"materiaisNecessarios\": \"\",\n" +
    	        "  \"beneficios\": \"\"\n" +
    	        "}]\n\n" +

    	        "Responda SOMENTE com o JSON.\n\n" +
    	        "Dados da criança: %d anos, gênero %s.",
    	        crianca.getIdade(),
    	        crianca.getGenero()
    	);




        String json = openAIService.generateText(prompt);

        // DEBUG
        System.out.println("====== JSON RECEBIDO DA OPENAI ======");
        System.out.println(json);
        System.out.println("====================================");

        JSONArray array = new JSONArray(json);
        List<Tarefa> tarefas = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject o = array.getJSONObject(i);

            Tarefa t = Tarefa.builder()
                    .titulo(o.getString("titulo"))
                    .descricao(o.getString("descricao"))
                    .areaDesenvolvimento(Tarefa.AreaDesenvolvimento.valueOf(o.getString("areaDesenvolvimento")))
                    .categoria(o.getString("categoria"))
                    .faixaEtariaMin(o.getInt("faixaEtariaMin"))
                    .faixaEtariaMax(o.getInt("faixaEtariaMax"))
                    .nivelDificuldade(o.getString("nivelDificuldade"))
                    .duracaoEstimadaMinutos(o.getInt("duracaoEstimadaMinutos"))
                    .materiaisNecessarios(o.getString("materiaisNecessarios"))
                    .beneficios(o.getString("beneficios"))
                    .ativo(true)
                    .build();

            tarefas.add(t);
        }

        return tarefas;
    }
}
