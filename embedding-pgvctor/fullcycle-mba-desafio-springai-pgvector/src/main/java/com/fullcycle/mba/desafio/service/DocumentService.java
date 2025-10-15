package com.fullcycle.mba.desafio.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {

    @Autowired
    private VectorStore vectorStore;

    public void ingestPdf(String pdfPath) throws IOException {
        System.out.println("📄 Iniciando ingestão do PDF: " + pdfPath);
        
        // Ler PDF
        List<Document> documents = readPdf(pdfPath);
        
        // Fazer chunking
        List<Document> chunks = chunkDocuments(documents);
        
        // Armazenar no vector store
        vectorStore.add(chunks);
        
        System.out.println("✅ Ingestão concluída com " + chunks.size() + " chunks");
    }

    private List<Document> readPdf(String pdfPath) throws IOException {
        List<Document> documents = new ArrayList<>();
        
        // Tentar carregar do classpath primeiro, depois do sistema de arquivos
        InputStream inputStream = null;
        try {
            ClassPathResource resource = new ClassPathResource(pdfPath);
            if (resource.exists()) {
                inputStream = resource.getInputStream();
                System.out.println("📁 Carregando PDF do classpath: " + pdfPath);
            } else {
                // Fallback para arquivo do sistema
                inputStream = new java.io.FileInputStream(pdfPath);
                System.out.println("📁 Carregando PDF do sistema de arquivos: " + pdfPath);
            }
        } catch (Exception e) {
            // Último recurso: tentar como arquivo do sistema
            inputStream = new java.io.FileInputStream(pdfPath);
            System.out.println("📁 Carregando PDF do sistema de arquivos (fallback): " + pdfPath);
        }
        
        try (PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            
            for (int page = 1; page <= document.getNumberOfPages(); page++) {
                stripper.setStartPage(page);
                stripper.setEndPage(page);
                
                String content = stripper.getText(document);
                
                if (!content.trim().isEmpty()) {
                    Document doc = new Document(
                        content,
                        Map.of(
                            "page", page,
                            "source", pdfPath,
                            "total_pages", document.getNumberOfPages()
                        )
                    );
                    documents.add(doc);
                }
            }
        }
        
        return documents;
    }

    private List<Document> chunkDocuments(List<Document> documents) {
        List<Document> chunks = new ArrayList<>();
        
        for (Document doc : documents) {
            String content = doc.getContent();
            
            // Chunking simples - dividir em pedaços de 1000 caracteres com overlap de 150
            int chunkSize = 1000;
            int overlap = 150;
            
            for (int i = 0; i < content.length(); i += chunkSize - overlap) {
                int end = Math.min(i + chunkSize, content.length());
                String chunkContent = content.substring(i, end);
                
                if (!chunkContent.trim().isEmpty()) {
                    Document chunk = new Document(
                        chunkContent,
                        Map.of(
                            "page", doc.getMetadata().get("page"),
                            "source", doc.getMetadata().get("source"),
                            "chunk_index", i / (chunkSize - overlap)
                        )
                    );
                    chunks.add(chunk);
                }
            }
        }
        
        return chunks;
    }
}
