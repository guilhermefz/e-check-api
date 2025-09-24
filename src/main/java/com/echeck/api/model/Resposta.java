package com.echeck.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "resposta")
@Table(name = "resposta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resposta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "id_reserva")
    private long reserva;
    @JoinColumn(name = "id_pergunta")
    private long pergunta;
    private String texto;
    private Integer nota;
}
