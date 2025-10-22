package com.echeck.api.services;

import com.echeck.api.Dtos.UsuarioDto;
import com.echeck.api.model.Usuario;
import com.echeck.api.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    public Usuario create(UsuarioDto dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setTipo(dto.getTipo());
        usuario.setSenha(dto.getSenha());
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listar() {
        return this.usuarioRepository.findAll();
    }

    public void delete(long id) {
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("produto não encontrado"));
        usuarioRepository.delete(existente);
    }

    public List<Usuario> buscarPorNome(String nomeBusca) {
        return usuarioRepository.findByNomeContainingIgnoreCase(nomeBusca);
    }
}
