package com.echeck.api.services;

import com.echeck.api.Dtos.RespostaDto;
import com.echeck.api.model.Pergunta;
import com.echeck.api.model.Resposta;
import com.echeck.api.model.enums.TipoPergunta;
import com.echeck.api.repositories.PerguntaRepository;
import com.echeck.api.repositories.ReservaRepository;
import com.echeck.api.repositories.RespostaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RespostaService {
    private final RespostaRepository respostaRepository;
    private final PerguntaRepository perguntaRepository; // Para validar a Pergunta
    private final ReservaRepository reservaRepository;

    public RespostaService(RespostaRepository respostaRepository, PerguntaRepository perguntaRepository, ReservaRepository reservaRepository){
        this.respostaRepository = respostaRepository;
        this.perguntaRepository = perguntaRepository;
        this.reservaRepository = reservaRepository;
    }

    @Transactional
    public Resposta create(RespostaDto dto) {

        if (dto.getReservaId() == null) {
            throw new IllegalArgumentException("O ID da Reserva é obrigatório.");
        }
        if (dto.getPerguntaId() == null) {
            throw new IllegalArgumentException("O ID da Pergunta é obrigatório.");
        }

        if (!reservaRepository.existsById(dto.getReservaId())) {
            throw new RuntimeException("Reserva não encontrada! ID: " + dto.getReservaId());
        }

        Pergunta pergunta = perguntaRepository.findById(dto.getPerguntaId())
                .orElseThrow(() -> new RuntimeException("Pergunta não encontrada! ID: " + dto.getPerguntaId()));

        Resposta resposta = new Resposta();
        resposta.setReserva(dto.getReservaId());
        resposta.setPergunta(dto.getPerguntaId());

        if (pergunta.getTipo() == TipoPergunta.TEXTO_ABERTO || pergunta.getTipo() == TipoPergunta.MULTIPLA_ESCOLHA) {
            if (dto.getTexto() == null || dto.getTexto().trim().isEmpty()) {
                throw new IllegalArgumentException("O campo 'texto' é obrigatório para este tipo de pergunta.");
            }
            resposta.setTexto(dto.getTexto());
            resposta.setNota(null);

        } else if (pergunta.getTipo() == TipoPergunta.ESTRELAS) {
            if (dto.getNota() == null) {
                throw new IllegalArgumentException("O campo 'nota' é obrigatório para este tipo de pergunta.");
            }
            resposta.setNota(dto.getNota());
            resposta.setTexto(null);
        }

        return respostaRepository.save(resposta);
    }

    public Optional<Resposta> findById(Long id) {
        return respostaRepository.findById(id);
    }

    public List<Resposta> findByReservaId(Long reservaId) {
        if (!reservaRepository.existsById(reservaId)) {
            throw new RuntimeException("Reserva não encontrada! ID: " + reservaId);
        }
        return respostaRepository.findByReserva(reservaId);
    }

    public List<Resposta> findByPerguntaId(Long perguntaId) {
        if (!perguntaRepository.existsById(perguntaId)) {
            throw new RuntimeException("Pergunta não encontrada! ID: " + perguntaId);
        }
        return respostaRepository.findByPergunta(perguntaId);
    }

    @Transactional
    public void delete(Long id) {
        if (!respostaRepository.existsById(id)) {
            throw new RuntimeException("Resposta não encontrada! ID: " + id);
        }
        respostaRepository.deleteById(id);
    }
}
