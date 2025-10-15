package com.fullcycle.mba.desafio.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private ChatClient chatClient;

    private static final String PROMPT_TEMPLATE = """
        CONTEXTO:
        {contexto}

        REGRAS:
        - Responda somente com base no CONTEXTO.
        - O contexto contém nomes, valores de reais de faturamento e ano de fundação de cada empresa.
        - Se a informação não estiver explicitamente no CONTEXTO, responda:
          "Não tenho informações necessárias para responder sua pergunta."
        - Nunca invente ou use conhecimento externo.
        - Nunca produza opiniões ou interpretações além do que está escrito.

        EXEMPLOS DE PERGUNTAS FORA DO CONTEXTO:
        Pergunta: "Qual é a capital da França?"
        Resposta: "Não tenho informações necessárias para responder sua pergunta."

        Pergunta: "Quantos clientes temos em 2024?"
        Resposta: "Não tenho informações necessárias para responder sua pergunta."

        Pergunta: "Você acha isso bom ou ruim?"
        Resposta: "Não tenho informações necessárias para responder sua pergunta."

        PERGUNTA DO USUÁRIO:
        {pergunta}

        RESPONDA A "PERGUNTA DO USUÁRIO"
        """;

    public String searchAndAnswer(String question) {
        System.out.println("🔍 Buscando documentos relevantes para: " + question);
        
        try {
            // Busca semântica
            List<Document> relevantDocs = vectorStore.similaritySearch(
                SearchRequest.query(question).withTopK(5)
            );

            if (relevantDocs.isEmpty()) {
                return "❌ Nenhum documento relevante encontrado para responder sua pergunta.";
            }

            // Formatar contexto
            String context = formatContext(relevantDocs);
            
            // Criar prompt final
            String prompt = PROMPT_TEMPLATE
                .replace("{contexto}", context)
                .replace("{pergunta}", question);

            // Gerar resposta com LLM
            System.out.println("💭 Gerando resposta com LLM...");
            String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

            return response;

        } catch (Exception e) {
            System.err.println("❌ Erro ao processar pergunta: " + e.getMessage());
            return "❌ Erro ao processar sua pergunta: " + e.getMessage();
        }
    }

    private String formatContext(List<Document> documents) {
        return documents.stream()
            .map(doc -> {
                String content = doc.getContent();
                String metadata = doc.getMetadata().toString();
                return String.format("Conteúdo: %s\nMetadados: %s", content, metadata);
            })
            .collect(Collectors.joining("\n\n---\n\n"));
    }
}
