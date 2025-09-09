package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "formulario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Formulario {

    private Long id;
    private String nome;
    private Boolean status;
    @JoinColumn(name = "id_unidade")
    private Unidade unidade;
}
