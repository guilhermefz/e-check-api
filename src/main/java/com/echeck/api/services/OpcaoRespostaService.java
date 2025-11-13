package com.echeck.api.services;

import com.echeck.api.Dtos.OpcaoRespostaDto;
import com.echeck.api.model.OpcaoResposta;
import com.echeck.api.model.Pergunta;
import com.echeck.api.model.enums.TipoPergunta;
import com.echeck.api.repositories.OpcaoRespostaRepository;
import com.echeck.api.repositories.PerguntaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OpcaoRespostaService {
    private final OpcaoRespostaRepository opcaoRespostaRepository;
    private final PerguntaRepository perguntaRepository;

    public OpcaoRespostaService(OpcaoRespostaRepository opcaoRespostaRepository, PerguntaRepository perguntaRepository)
    {
        this.opcaoRespostaRepository = opcaoRespostaRepository;
        this.perguntaRepository = perguntaRepository;
    }

    @Transactional
    public OpcaoResposta create(OpcaoRespostaDto dto) {
        if (dto.getPerguntaId() == null) {
            throw new IllegalArgumentException("O ID da Pergunta é obrigatório para criar uma opção.");
        }

        Pergunta perguntaPai = perguntaRepository.findById(dto.getPerguntaId())
                .orElseThrow(() -> new RuntimeException("Pergunta não encontrada! ID: " + dto.getPerguntaId()));

        if (perguntaPai.getTipo() != TipoPergunta.MULTIPLA_ESCOLHA) {
            throw new IllegalArgumentException("Não é possível adicionar opções a uma pergunta que não seja de MULTIPLA_ESCOLHA. " +
                    "Tipo atual: " + perguntaPai.getTipo());
        }

        OpcaoResposta novaOpcao = new OpcaoResposta();
        novaOpcao.setOpcao(dto.getOpcao());
        novaOpcao.setPergunta(perguntaPai);

        return opcaoRespostaRepository.save(novaOpcao);
    }

    public Optional<OpcaoResposta> findById(Long id) {
        return opcaoRespostaRepository.findById(id);
    }

    public List<OpcaoResposta> findByPerguntaId(Long perguntaId) {
        if (!perguntaRepository.existsById(perguntaId)) {
            throw new RuntimeException("Pergunta não encontrada! ID: " + perguntaId);
        }
        return opcaoRespostaRepository.findByPerguntaId(perguntaId);
    }

    @Transactional
    public OpcaoResposta update(Long id, OpcaoRespostaDto dto) {
        OpcaoResposta opcaoExistente = opcaoRespostaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opção de Resposta não encontrada! ID: " + id));

        Pergunta perguntaPai = perguntaRepository.findById(dto.getPerguntaId())
                .orElseThrow(() -> new RuntimeException("Pergunta não encontrada! ID: " + dto.getPerguntaId()));

        if (perguntaPai.getTipo() != TipoPergunta.MULTIPLA_ESCOLHA) {
            throw new IllegalArgumentException("Não é possível associar esta opção a uma pergunta do tipo: " + perguntaPai.getTipo());
        }

        opcaoExistente.setOpcao(dto.getOpcao());
        opcaoExistente.setPergunta(perguntaPai);

        return opcaoRespostaRepository.save(opcaoExistente);
    }

    @Transactional
    public void delete(Long id) {
        if (!opcaoRespostaRepository.existsById(id)) {
            throw new RuntimeException("Opção de Resposta não encontrada! ID: " + id);
        }
        opcaoRespostaRepository.deleteById(id);
    }
}
