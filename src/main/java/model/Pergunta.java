package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.TipoPergunta;

@Entity
@Table(name = "pergunta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pergunta {

    private Long id;
    private String descricao;
    @Column(name = "tipo")
    private TipoPergunta tipo;
    @JoinColumn(name = "id_formulario")
    private Formulario formulario;
}
