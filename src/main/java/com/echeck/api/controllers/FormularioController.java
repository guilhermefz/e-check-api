package com.echeck.api.controllers;

import com.echeck.api.Dtos.FormularioDto;
import com.echeck.api.model.Formulario;
import com.echeck.api.services.FormularioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/formulario")
public class FormularioController {

    private final FormularioService formularioService;

    public FormularioController(FormularioService formularioService) {
        this.formularioService = formularioService;
    }

    @PostMapping("/salvar")
    public ResponseEntity<?> salvar(@RequestBody FormularioDto dto) {
        try {
            Formulario formularioSalvo = formularioService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(formularioSalvo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/listar")
    public List<Formulario> listarTodos() {
        return formularioService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Formulario> buscarPorId(@PathVariable Long id) {
        Optional<Formulario> formulario = formularioService.findById(id);

        if (formulario.isPresent()) {
            return ResponseEntity.ok(formulario.get()); // 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody FormularioDto dto) {
        try {
            Formulario formularioAtualizado = formularioService.update(id, dto);
            return ResponseEntity.ok(formularioAtualizado); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/apagar/{id}")
    public ResponseEntity<String> apagar(@PathVariable Long id) {
        try {
            formularioService.delete(id);
            return ResponseEntity.ok("Formulário apagado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/unidade/{unidadeId}")
    public ResponseEntity<List<Formulario>> buscarPorUnidade(@PathVariable Long unidadeId) {
        List<Formulario> formularios = formularioService.buscarPorUnidadeId(unidadeId);
        return ResponseEntity.ok(formularios);
    }
}
