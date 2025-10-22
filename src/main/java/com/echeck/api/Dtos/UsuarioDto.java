package com.echeck.api.Dtos;

import com.echeck.api.model.Formulario;
import com.echeck.api.model.enums.TipoUsuario;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NonNull;
import org.antlr.v4.runtime.misc.NotNull;
import org.aspectj.bridge.IMessage;

import java.util.List;
@Data
public class UsuarioDto {
    private String nome;
    private String email;
    private String senha;
    private TipoUsuario tipo;
}
