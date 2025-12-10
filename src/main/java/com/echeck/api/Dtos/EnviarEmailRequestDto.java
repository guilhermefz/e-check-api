package com.echeck.api.Dtos;

import lombok.Data;

@Data
public class EnviarEmailRequestDto {
    private Long reservaId;
    private String email; // O email de destino fornecido pelo front
    private String link;  // O link completo do formulário (ex: http://44.201.180.209/responder-form/...)
}