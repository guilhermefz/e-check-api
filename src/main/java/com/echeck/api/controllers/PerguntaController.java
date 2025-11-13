package com.echeck.api.controllers;

import com.echeck.api.Dtos.PerguntaDto;
import com.echeck.api.Dtos.UsuarioDto;
import com.echeck.api.model.Pergunta;
import com.echeck.api.model.Usuario;
import com.echeck.api.services.PerguntaService;
import com.echeck.api.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/pergunta")
public class PerguntaController {

    private final PerguntaService perguntaService;

    public PerguntaController(PerguntaService perguntaService) {
        this.perguntaService = perguntaService;
    }

    @PostMapping("/salvar")
    public ResponseEntity<?> salvar(@RequestBody PerguntaDto dto) { // Recebe PerguntaDto no corpo
        try {
            Pergunta perguntaSalva = perguntaService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(perguntaSalva);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Pergunta>> listarTodas() {
        List<Pergunta> perguntas = perguntaService.findAll();
        return ResponseEntity.ok(perguntas); // Retorna 200 OK + lista de perguntas
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pergunta> buscarPorId(@PathVariable Long id) {
        Optional<Pergunta> perguntaOpt = perguntaService.findById(id);

        return perguntaOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/formulario/{formularioId}")
    public ResponseEntity<?> buscarPorFormularioId(@PathVariable Long formularioId) {
        try {
            List<Pergunta> perguntas = perguntaService.findByFormularioId(formularioId);
            return ResponseEntity.ok(perguntas);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody PerguntaDto dto) {
        try {
            Pergunta perguntaAtualizada = perguntaService.update(id, dto);
            return ResponseEntity.ok(perguntaAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/apagar/{id}")
    public ResponseEntity<String> apagar(@PathVariable Long id) {
        try {
            perguntaService.delete(id);
            return ResponseEntity.ok("Pergunta com ID " + id + " apagada com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
