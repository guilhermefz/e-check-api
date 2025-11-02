package com.echeck.api.controllers;

import com.echeck.api.Dtos.OpcaoRespostaDto;
import com.echeck.api.model.OpcaoResposta;
import com.echeck.api.services.OpcaoRespostaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/opcao")
public class OpcaoRespostaController {

    private final OpcaoRespostaService opcaoRespostaService;

    public OpcaoRespostaController(OpcaoRespostaService opcaoRespostaService){
        this.opcaoRespostaService = opcaoRespostaService;
    }

    @PostMapping("/salvar")
    public ResponseEntity<?> salvar(@RequestBody OpcaoRespostaDto dto) {
        try {
            OpcaoResposta opcaoSalva = opcaoRespostaService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(opcaoSalva);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/pergunta/{perguntaId}")
    public ResponseEntity<?> buscarPorPerguntaId(@PathVariable Long perguntaId) {
        try {
            List<OpcaoResposta> opcoes = opcaoRespostaService.findByPerguntaId(perguntaId);
            return ResponseEntity.ok(opcoes);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody OpcaoRespostaDto dto) {
        try {
            OpcaoResposta opcaoAtualizada = opcaoRespostaService.update(id, dto);
            return ResponseEntity.ok(opcaoAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/apagar/{id}")
    public ResponseEntity<String> apagar(@PathVariable Long id) {
        try {
            opcaoRespostaService.delete(id);
            return ResponseEntity.ok("Opção de Resposta com ID " + id + " apagada com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
