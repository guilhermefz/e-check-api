package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.enums.StatusReserva;

import java.time.LocalDate;

@Entity
@Table(name = "reserva")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {


    private Long id;
    @JoinColumn(name = "id_unidade")
    private Unidade unidade;
    private String cpf;
    private String cnpj;
    private String telefone;
    private String email;
    private LocalDate dataCheckin;
    private LocalDate dataCheckout;
    private StatusReserva status;
}
