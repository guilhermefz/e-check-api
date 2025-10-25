package com.echeck.api.controllers;

import com.echeck.api.Dtos.LoginRequestDto;
import com.echeck.api.Dtos.UsuarioDto;
import com.echeck.api.model.Usuario;
import com.echeck.api.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @RequestMapping("/listar")
    public List<Usuario> listar() {
        return usuarioService.listar();
    }

    @PostMapping("/apagar/{id}")
    public ResponseEntity<String> apagar(@PathVariable(value = "id") long id) {
        try{
            usuarioService.delete(id);
            return ResponseEntity.ok("usuario apagado com sucesso!");
        }catch (Exception e) {
            //retorna error 500 com a mensagem de erro para o front
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: "+ e.getMessage());
        }

    }

    @GetMapping("/buscar/{nome}")
    public List<Usuario> buscar(@RequestParam String nomeBusca) {
        return usuarioService.buscarPorNome(nomeBusca);
    }

    @PostMapping("/login")
    public ResponseEntity<?> autenticarUsuario(@RequestBody LoginRequestDto loginDto) {

        Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(loginDto.getEmail());

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            if (usuario.getSenha().equals(loginDto.getSenha())) {

                usuario.setSenha(null);
                return ResponseEntity.ok(usuario);

            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha inválida");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
    }
}
