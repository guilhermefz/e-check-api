package com.echeck.api.services;

import com.echeck.api.Dtos.PerguntaDto;
import com.echeck.api.model.Formulario;
import com.echeck.api.model.Pergunta;
import com.echeck.api.repositories.FormularioRepository;
import com.echeck.api.repositories.PerguntaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PerguntaService {
    private final PerguntaRepository perguntaRepository;
    private final FormularioRepository formularioRepository;

    public PerguntaService(FormularioRepository formularioRepository, PerguntaRepository perguntaRepository){
        this.perguntaRepository = perguntaRepository;
        this.formularioRepository = formularioRepository;
    }

    @Transactional
    public Pergunta create(PerguntaDto dto) {

        Formulario formulario = formularioRepository.findById(dto.getFormularioId())
                .orElseThrow(() -> new RuntimeException("Formulário não encontrado! ID: " + dto.getFormularioId()));

        Pergunta pergunta = new Pergunta();
        pergunta.setDescricao(dto.getDescricao());
        pergunta.setTipo(dto.getTipo());
        pergunta.setFormulario(formulario);

        return perguntaRepository.save(pergunta);
    }

    public List<Pergunta> findAll() {
        return perguntaRepository.findAll();
    }

    public Optional<Pergunta> findById(Long id) {
        return perguntaRepository.findById(id);
    }

    public List<Pergunta> findByFormularioId(Long formularioId) {
        if (!formularioRepository.existsById(formularioId)) {
            throw new RuntimeException("Formulário não encontrado! ID: " + formularioId);
        }
        return perguntaRepository.findByFormularioId(formularioId);
    }

    @Transactional
    public Pergunta update(Long id, PerguntaDto dto) {
        Pergunta perguntaExistente = perguntaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pergunta não encontrada! ID: " + id));

        Formulario formulario = formularioRepository.findById(dto.getFormularioId())
                .orElseThrow(() -> new RuntimeException("Formulário não encontrado! ID: " + dto.getFormularioId()));

        perguntaExistente.setDescricao(dto.getDescricao());
        perguntaExistente.setTipo(dto.getTipo());
        perguntaExistente.setFormulario(formulario);

        return perguntaRepository.save(perguntaExistente);
    }

    @Transactional
    public void delete(Long id) {
        if (!perguntaRepository.existsById(id)) {
            throw new RuntimeException("Pergunta não encontrada! ID: " + id);
        }
        perguntaRepository.deleteById(id);
    }
}
