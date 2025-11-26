package com.echeck.api.controllers;

import com.echeck.api.Dtos.ReservaDto;
import com.echeck.api.model.Reserva;
import com.echeck.api.services.ReservaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/reserva")
public class ReservaController {
    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping("/salvar")
    public ResponseEntity<?> salvar(@RequestBody ReservaDto dto) {
        try {
            Reserva reservaSalva = reservaService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(reservaSalva);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Reserva>> listarTodas() {
        List<Reserva> reservas = reservaService.findAll();
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> buscarPorId(@PathVariable Long id) {
        Optional<Reserva> reservaOpt = reservaService.findById(id);

        return reservaOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody ReservaDto dto) {
        try {
            Reserva reservaAtualizada = reservaService.update(id, dto);
            return ResponseEntity.ok(reservaAtualizada);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/apagar/{id}")
    public ResponseEntity<String> apagar(@PathVariable Long id) {
        try {
            reservaService.delete(id);
            return ResponseEntity.ok("Reserva com ID " + id + " apagada com sucesso!");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/validar/{token}")
    public ResponseEntity<Reserva> buscarPorToken(@PathVariable String token) {
        Optional<Reserva> reserva = reservaService.findByToken(token);

        return reserva.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
