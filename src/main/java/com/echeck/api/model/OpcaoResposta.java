package com.echeck.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "opcaoresposta")
@Table(name = "opcaoresposta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpcaoResposta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String opcao;
    @ManyToOne
    @JoinColumn(name = "pergunta_id", nullable = false)
    private Pergunta pergunta;
}
