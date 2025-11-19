package com.echeck.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.echeck.api.model.enums.TipoUsuario;
import java.time.Instant;

@Entity(name = "usuario")
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private TipoUsuario tipo;

//CAMPOS PARA RECUPERAÇÃO DE SENHA
    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "token_expiration")
    private Instant tokenExpiration;

    public Usuario(String nome, String email, String senha, TipoUsuario tipo) {
        this.nome = nome;
        this.email = email;
        this.tipo = tipo;
        this.senha = senha;
    }

    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }
}
