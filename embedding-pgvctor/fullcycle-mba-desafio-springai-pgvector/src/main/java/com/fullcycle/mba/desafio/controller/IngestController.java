package com.fullcycle.mba.desafio.controller;

import com.fullcycle.mba.desafio.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/ingest")
@CrossOrigin(origins = "*")
public class IngestController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/pdf")
    public ResponseEntity<String> ingestPdf(@RequestBody IngestRequest request) {
        try {
            documentService.ingestPdf(request.getPdfPath());
            return ResponseEntity.ok("✅ PDF ingerido com sucesso!");
        } catch (IOException e) {
            return ResponseEntity.badRequest()
                .body("❌ Erro ao processar PDF: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("❌ Erro interno: " + e.getMessage());
        }
    }

    // DTO
    public static class IngestRequest {
        private String pdfPath;

        public String getPdfPath() {
            return pdfPath;
        }

        public void setPdfPath(String pdfPath) {
            this.pdfPath = pdfPath;
        }
    }
}
