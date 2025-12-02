package com.echeck.api.Dtos;

import com.echeck.api.model.enums.TipoPergunta;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ItemRelatorioDto {
    private String pergunta;
    private TipoPergunta tipo;
    private Double media;
    private List<String> comentarios;
    private Map<String, Long> contagemOpcoes;
}
