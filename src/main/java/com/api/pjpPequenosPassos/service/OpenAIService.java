package com.api.pjpPequenosPassos.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAIService {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    @Value("${openai.api.key:}")
    private String apiKey; // ← vem do application.properties


    public String generateText(String prompt) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException("Chave da OpenAI ausente. Configure em application.properties.");
        }

        try {
            RestTemplate restTemplate = new RestTemplate();

            // Reforço ao prompt
            String promptFinal = prompt +
                    "\n\nIMPORTANTE: Responda APENAS com JSON puro. Não use ```json, não use explicações.";

            JSONObject message = new JSONObject()
                    .put("role", "user")
                    .put("content", promptFinal);

            JSONObject requestBody = new JSONObject()
                    .put("model", "gpt-4o-mini")
                    .put("messages", new JSONArray().put(message))
                    .put("max_tokens", 500);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<String> request =
                    new HttpEntity<>(requestBody.toString(), headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(API_URL, HttpMethod.POST, request, String.class);

            String conteudo = new JSONObject(response.getBody())
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");

            conteudo = conteudo.trim();

            // 🔥 REMOVER blocos de markdown ```json ``` se existirem
            conteudo = conteudo.replace("```json", "")
                               .replace("```", "")
                               .trim();

            // 🔥 Detectar automaticamente se é ARRAY ou OBJETO
            int inicioArray = conteudo.indexOf("[");
            int fimArray = conteudo.lastIndexOf("]");
            int inicioObj = conteudo.indexOf("{");
            int fimObj = conteudo.lastIndexOf("}");

            if (inicioArray >= 0 && fimArray >= 0) {
                conteudo = conteudo.substring(inicioArray, fimArray + 1);
            } else if (inicioObj >= 0 && fimObj >= 0) {
                conteudo = conteudo.substring(inicioObj, fimObj + 1);
            } else {
                throw new RuntimeException("A resposta não contém JSON válido:\n" + conteudo);
            }

            System.out.println("📦 JSON LIMPO FINAL:");
            System.out.println(conteudo);

            return conteudo;

        } catch (Exception e) {
            throw new RuntimeException("Falha ao gerar texto com OpenAI: " + e.getMessage());
        }
    }
}


