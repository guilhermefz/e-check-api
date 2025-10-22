package com.echeck.api.Dtos;

import lombok.Data;

@Data
public class UnidadeDto {

    private String nome;
    private String cnpj;
    private String pais;
    private String uf;
    private String cidade;
    private String bairro;
    private String rua;
    private String numero;
}
