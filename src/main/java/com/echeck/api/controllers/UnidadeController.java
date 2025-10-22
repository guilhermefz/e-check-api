package com.echeck.api.controllers;

import com.echeck.api.Dtos.UnidadeDto;
import com.echeck.api.model.Unidade;
import com.echeck.api.services.UnidadeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/unidade")
public class UnidadeController {

    private final UnidadeService unidadeService;

    public UnidadeController (UnidadeService unidadeService){
        this.unidadeService = unidadeService;
    }

    @PostMapping("/salvar")
    public ResponseEntity<Unidade> salvar(@RequestBody UnidadeDto dto) {
        Unidade unidadeSalva = unidadeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(unidadeSalva);
    }

    @GetMapping("/listar")
    public List<Unidade> listarTodas() {
        return unidadeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Unidade> buscarPorId(@PathVariable Long id) {
        Optional<Unidade> unidade = unidadeService.findById(id);

        if (unidade.isPresent()) {
            return ResponseEntity.ok(unidade.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Unidade> atualizar(@PathVariable Long id, @RequestBody UnidadeDto dto) {
        try {
            Unidade unidadeAtualizada = unidadeService.update(id, dto);
            return ResponseEntity.ok(unidadeAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/apagar/{id}")
    public ResponseEntity<String> apagar(@PathVariable Long id) {
        try {
            unidadeService.delete(id);
            return ResponseEntity.ok("Unidade apagada com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
