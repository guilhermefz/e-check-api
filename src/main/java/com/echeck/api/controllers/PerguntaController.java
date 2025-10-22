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
@RequestMapping("/pergunta")
public class PerguntaController {

    @GetMapping("/hello")
    public String helloWorld() {
        return "Olá, mundo!";
    }
}
