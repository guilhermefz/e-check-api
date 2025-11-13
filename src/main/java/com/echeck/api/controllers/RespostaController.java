package com.echeck.api.controllers;

import com.echeck.api.Dtos.RespostaDto;
import com.echeck.api.model.Resposta;
import com.echeck.api.services.RespostaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/resposta")
public class RespostaController {
    private final RespostaService respostaService;

    public RespostaController(RespostaService respostaService) {
        this.respostaService = respostaService;
    }

    @PostMapping("/salvar")
    public ResponseEntity<?> salvar(@RequestBody RespostaDto dto) {
        try {
            Resposta respostaSalva = respostaService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(respostaSalva);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/reserva/{reservaId}")
    public ResponseEntity<?> buscarPorReservaId(@PathVariable Long reservaId) {
        try {
            List<Resposta> respostas = respostaService.findByReservaId(reservaId);
            return ResponseEntity.ok(respostas);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/pergunta/{perguntaId}")
    public ResponseEntity<?> buscarPorPerguntaId(@PathVariable Long perguntaId) {
        try {
            List<Resposta> respostas = respostaService.findByPerguntaId(perguntaId);
            return ResponseEntity.ok(respostas);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/apagar/{id}")
    public ResponseEntity<String> apagar(@PathVariable Long id) {
        try {
            respostaService.delete(id);
            return ResponseEntity.ok("Resposta com ID " + id + " apagada com sucesso!");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
