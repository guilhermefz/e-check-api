package com.echeck.api.Dtos;

import com.echeck.api.model.enums.StatusReserva;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservaDto {

    private Long unidadeId;
    private Long formularioId;
    private String cpf;
    private String cnpj;
    private String telefone;
    private String email;
    private LocalDate dataCheckin;
    private LocalDate dataCheckout;
    private StatusReserva status;
}
