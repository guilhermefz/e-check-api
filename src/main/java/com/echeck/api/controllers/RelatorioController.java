package com.echeck.api.controllers;

import com.echeck.api.Dtos.RelatorioDto;
import com.echeck.api.services.PdfService;
import com.echeck.api.services.RelatorioService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/relatorio")
public class RelatorioController {
    private final RelatorioService relatorioService;
    private final PdfService pdfService;

    public RelatorioController(RelatorioService relatorioService, PdfService pdfService) {
        this.relatorioService = relatorioService;
        this.pdfService = pdfService;
    }

    @GetMapping("/{formularioId}")
    public ResponseEntity<?> gerarRelatorio(@PathVariable Long formularioId) {
        try {
            RelatorioDto relatorio = relatorioService.gerarRelatorio(formularioId);
            return ResponseEntity.ok(relatorio);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{formularioId}/pdf")
    public ResponseEntity<InputStreamResource> baixarPdf(@PathVariable Long formularioId) {
        RelatorioDto relatorio = relatorioService.gerarRelatorio(formularioId);
        ByteArrayInputStream bis = pdfService.gerarPdfRelatorio(relatorio);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=relatorio-satisfacao.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
