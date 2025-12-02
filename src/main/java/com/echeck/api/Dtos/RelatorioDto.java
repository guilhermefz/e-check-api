package com.echeck.api.Dtos;

import lombok.Data;

import java.util.List;

@Data
public class RelatorioDto {
    private String nomeFormulario;
    private Long totalAvaliacoes;
    private List<ItemRelatorioDto> itens;
}
