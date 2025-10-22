package com.echeck.api.services;

import com.echeck.api.Dtos.FormularioDto;
import com.echeck.api.model.Formulario;
import com.echeck.api.repositories.FormularioRepository;
import com.echeck.api.repositories.UnidadeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FormularioService {

    private final FormularioRepository formularioRepository;
    private final UnidadeRepository unidadeRepository;

    public FormularioService(FormularioRepository formularioRepository, UnidadeRepository unidadeRepository){
        this.formularioRepository = formularioRepository;
        this.unidadeRepository = unidadeRepository;
    }

    @Transactional
    public Formulario create(FormularioDto dto) {

        boolean unidadeExiste = unidadeRepository.existsById(dto.getUnidadeId());

        if (!unidadeExiste) {
            throw new RuntimeException("Unidade não encontrada! Impossível criar formulário.");
        }

        Formulario formulario = new Formulario();
        formulario.setNome(dto.getNome());
        formulario.setStatus(dto.getStatus());
        formulario.setUnidadeId(dto.getUnidadeId());

        return formularioRepository.save(formulario);
    }

    public List<Formulario> findAll() {
        return formularioRepository.findAll();
    }

    public Optional<Formulario> findById(Long id) {
        return formularioRepository.findById(id);
    }

    @Transactional
    public Formulario update(Long id, FormularioDto dto) {
        Formulario formularioExistente = formularioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formulário não encontrado!"));

        boolean unidadeExiste = unidadeRepository.existsById(dto.getUnidadeId());
        if (!unidadeExiste) {
            throw new RuntimeException("Unidade não encontrada! Impossível atualizar formulário.");
        }

        formularioExistente.setNome(dto.getNome());
        formularioExistente.setStatus(dto.getStatus());
        formularioExistente.setUnidadeId(dto.getUnidadeId());

        return formularioRepository.save(formularioExistente);
    }

    @Transactional
    public void delete(Long id) {
        Formulario formularioExistente = formularioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formulário não encontrado!"));

        formularioRepository.delete(formularioExistente);
    }

}
