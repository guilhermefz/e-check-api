package com.echeck.api.services;

import com.echeck.api.Dtos.ReservaDto;
import com.echeck.api.model.Formulario;
import com.echeck.api.model.Reserva;
import com.echeck.api.repositories.FormularioRepository;
import com.echeck.api.repositories.ReservaRepository;
import com.echeck.api.repositories.UnidadeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReservaService {
    private final ReservaRepository reservaRepository;
    private final UnidadeRepository unidadeRepository;
    private final FormularioRepository formularioRepository;

    public ReservaService(ReservaRepository reservaRepository, UnidadeRepository unidadeRepository, FormularioRepository formularioRepository){
        this.reservaRepository = reservaRepository;
        this.unidadeRepository = unidadeRepository;
        this.formularioRepository = formularioRepository;
    }

    @Transactional
    public Reserva create(ReservaDto dto) {

        if (dto.getUnidadeId() == null) {
            throw new IllegalArgumentException("O ID da Unidade é obrigatório para criar a reserva.");
        }

        if (!unidadeRepository.existsById(dto.getUnidadeId())) {
            throw new RuntimeException("Unidade não encontrada! ID: " + dto.getUnidadeId());
        }

        Reserva reserva = new Reserva();
        reserva.setToken(UUID.randomUUID().toString());
        Formulario formularioEscolhido = null;

        if (dto.getFormularioId() != null) {
            formularioEscolhido = formularioRepository.findById(dto.getFormularioId())
                    .orElseThrow(() -> new RuntimeException("Formulário informado não encontrado! ID: " + dto.getFormularioId()));
        }else {
            List<Formulario> formularios = formularioRepository.findByUnidadeId(dto.getUnidadeId());
            formularioEscolhido = formularios.stream()
                    .filter(f -> Boolean.TRUE.equals(f.getStatus()))
                    .findFirst()
                    .orElse(null);
        }

        if (formularioEscolhido != null) {
            reserva.setFormulario(formularioEscolhido);
        } else {
            System.out.println("AVISO: Nenhum formulário ativo encontrado para a unidade " + dto.getUnidadeId());
        }

        reserva.setUnidade(dto.getUnidadeId());
        reserva.setCpf(dto.getCpf());
        reserva.setCnpj(dto.getCnpj());
        reserva.setTelefone(dto.getTelefone());
        reserva.setEmail(dto.getEmail());
        reserva.setDataCheckin(dto.getDataCheckin());
        reserva.setDataCheckout(dto.getDataCheckout());
        reserva.setStatus(dto.getStatus());

        return reservaRepository.save(reserva);
    }

    public List<Reserva> findAll() {
        return reservaRepository.findAll();
    }

    public Optional<Reserva> findById(Long id) {
        return reservaRepository.findById(id);
    }

    @Transactional
    public Reserva update(Long id, ReservaDto dto) {

        Reserva reservaExistente = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada! ID: " + id));

        if (dto.getUnidadeId() == null) {
            throw new IllegalArgumentException("O ID da Unidade é obrigatório.");
        }
        if (!unidadeRepository.existsById(dto.getUnidadeId())) {
            throw new RuntimeException("Unidade não encontrada! ID: " + dto.getUnidadeId());
        }

        reservaExistente.setUnidade(dto.getUnidadeId());
        reservaExistente.setCpf(dto.getCpf());
        reservaExistente.setCnpj(dto.getCnpj());
        reservaExistente.setTelefone(dto.getTelefone());
        reservaExistente.setEmail(dto.getEmail());
        reservaExistente.setDataCheckin(dto.getDataCheckin());
        reservaExistente.setDataCheckout(dto.getDataCheckout());
        reservaExistente.setStatus(dto.getStatus());

        return reservaRepository.save(reservaExistente);
    }

    @Transactional
    public void delete(Long id) {

        if (!reservaRepository.existsById(id)) {
            throw new RuntimeException("Reserva não encontrada! ID: " + id);
        }

        reservaRepository.deleteById(id);
    }

    public Optional<Reserva> findByToken(String token) {
        return reservaRepository.findByToken(token);
    }
}

