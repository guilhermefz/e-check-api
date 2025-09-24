package com.echeck.api.controllers;

import com.echeck.api.Dtos.UsuarioDto;
import com.echeck.api.model.Usuario;
import com.echeck.api.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @GetMapping("/hello")
    public String helloWorld() {
        return "Olá, mundo!";
    }

    @PostMapping("/salvar")
    public ResponseEntity<?> salvar(
            @RequestBody @Validated UsuarioDto dto
    ) {

        Usuario userModel = usuarioService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                userModel);
    }
}
