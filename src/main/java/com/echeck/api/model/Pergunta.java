package com.echeck.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.echeck.api.model.enums.TipoPergunta;

import java.util.List;

@Entity(name = "pergunta")
@Table(name = "pergunta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pergunta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;
    @Column(name = "tipo")
    @Enumerated(EnumType.STRING)
    private TipoPergunta tipo;
    @ManyToOne
    @JoinColumn(name = "id_formulario")
    private Formulario formulario;
    @OneToMany(mappedBy = "pergunta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpcaoResposta> opcoesRespostas;
}
