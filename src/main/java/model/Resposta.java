package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resposta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resposta {

    private Long id;
    @JoinColumn(name = "id_reserva")
    private Reserva reserva;
    @JoinColumn(name = "id_pergunta")
    private Pergunta pergunta;
    private String texto;
    private Integer nota;
}
