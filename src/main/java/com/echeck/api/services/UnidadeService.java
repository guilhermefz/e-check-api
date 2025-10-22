package com.echeck.api.services;

import com.echeck.api.Dtos.UnidadeDto;
import com.echeck.api.model.Unidade;
import com.echeck.api.repositories.UnidadeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnidadeService {
    private final UnidadeRepository unidadeRepository;

    public UnidadeService(UnidadeRepository unidadeRepository) {
        this.unidadeRepository = unidadeRepository;
    }

    @Transactional
    public Unidade create(UnidadeDto dto) {
        Unidade unidade = new Unidade();
        unidade.setNome(dto.getNome());
        unidade.setCnpj(dto.getCnpj());
        unidade.setPais(dto.getPais());
        unidade.setUf(dto.getUf());
        unidade.setCidade(dto.getCidade());
        unidade.setBairro(dto.getBairro());
        unidade.setRua(dto.getRua());
        unidade.setNumero(dto.getNumero());

        return unidadeRepository.save(unidade);
    }

    public List<Unidade> findAll() {
        return unidadeRepository.findAll();
    }

    public Optional<Unidade> findById(Long id) {
        return unidadeRepository.findById(id);
    }

    @Transactional
    public Unidade update(Long id, UnidadeDto dto) {
        Unidade unidadeExistente = unidadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Unidade não encontrada!"));

        unidadeExistente.setNome(dto.getNome());
        unidadeExistente.setCnpj(dto.getCnpj());
        unidadeExistente.setPais(dto.getPais());
        unidadeExistente.setUf(dto.getUf());
        unidadeExistente.setCidade(dto.getCidade());
        unidadeExistente.setBairro(dto.getBairro());
        unidadeExistente.setRua(dto.getRua());
        unidadeExistente.setNumero(dto.getNumero());

        return unidadeRepository.save(unidadeExistente);
    }

    @Transactional
    public void delete(Long id) {
        Unidade unidadeExistente = unidadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Unidade não encontrada!"));
        unidadeRepository.delete(unidadeExistente);
    }

    public List<Unidade> buscarPorNome(String nomeBusca) {
        return unidadeRepository.findByNomeContainingIgnoreCase(nomeBusca);
    }
}
