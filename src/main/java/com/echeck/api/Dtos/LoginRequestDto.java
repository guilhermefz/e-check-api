package com.echeck.api.Dtos;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String senha;
}
