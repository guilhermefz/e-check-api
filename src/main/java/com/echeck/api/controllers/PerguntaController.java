package com.echeck.api.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/perguntas")
public class PerguntaController {

    @GetMapping("/hello")
    public String helloWorld() {
        return "Olá, mundo!";
    }
}
