package com.echeck.api.Dtos;

import com.echeck.api.model.enums.TipoPergunta;
import lombok.Data;

@Data
public class PerguntaDto {

    private String Descricao;
    private TipoPergunta Tipo;
    private Long FormularioId;
}
