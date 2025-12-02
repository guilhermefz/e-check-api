package com.echeck.api.controllers;

import com.echeck.api.Dtos.RelatorioDto;
import com.echeck.api.services.RelatorioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/relatorio")
public class RelatorioController {
    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
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
}
