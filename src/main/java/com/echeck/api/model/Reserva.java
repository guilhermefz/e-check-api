package com.echeck.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.echeck.api.model.enums.StatusReserva;

import java.time.LocalDate;

@Entity(name = "reserva")
@Table(name = "reserva")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "id_unidade")
    private long unidade;
    private String cpf;
    private String cnpj;
    private String telefone;
    private String email;
    private LocalDate dataCheckin;
    private LocalDate dataCheckout;
    private StatusReserva status;

    private String token;
    @ManyToOne
    @JoinColumn(name = "id_formulario")
    @JsonIgnoreProperties("perguntas")
    private Formulario formulario;
}
