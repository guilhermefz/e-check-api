package com.echeck.api.services;

import com.echeck.api.Dtos.UsuarioDto;
import com.echeck.api.model.Usuario;
import com.echeck.api.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    // ATRIBUTOS
    private final UsuarioRepository usuarioRepository;
    // Embora o AuthService use o SendGrid, vamos injetar aqui para manter o construtor
    // sincronizado com o que você havia tentado, mas ele não será usado nesta classe.
    private final SendGridEmailService emailService;

    // CONSTRUTOR UNIFICADO (Injeção de Dependência)
    public UsuarioService(UsuarioRepository usuarioRepository, SendGridEmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
    }


    public Usuario create(UsuarioDto dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setTipo(dto.getTipo());
        usuario.setSenha(dto.getSenha());
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario update(Long id, UsuarioDto dto) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        usuarioExistente.setNome(dto.getNome());
        usuarioExistente.setEmail(dto.getEmail());
        usuarioExistente.setTipo(dto.getTipo());

        // Só atualiza a senha se foi fornecida uma nova
        if(dto.getSenha() != null && !dto.getSenha().isEmpty()) {
            usuarioExistente.setSenha(dto.getSenha());
        }

        return usuarioRepository.save(usuarioExistente);
    }

    public List<Usuario> listar() {
        return this.usuarioRepository.findAll();
    }

    public void delete(long id) {
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        usuarioRepository.delete(existente);
    }

    public List<Usuario> buscarPorNome(String nomeBusca) {
        return usuarioRepository.findByNomeContainingIgnoreCase(nomeBusca);
    }

    // MÉTODO ESSENCIAL: Permite que outras classes busquem o usuário pelo e-mail
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
}