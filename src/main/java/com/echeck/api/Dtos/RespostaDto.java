package com.echeck.api.Dtos;

import lombok.Data;

@Data
public class RespostaDto {

    private Long reservaId;
    private Long perguntaId;
    private String texto;
    private Integer nota;
}
