package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "opcaoresposta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpcaoResposta {

    private Long id;
    private String opcao;
    @JoinColumn(name = "id_pergunta")
    private Pergunta pergunta;
}
