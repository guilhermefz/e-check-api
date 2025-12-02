package com.echeck.api.services;

import com.echeck.api.Dtos.ItemRelatorioDto;
import com.echeck.api.Dtos.RelatorioDto;
import com.echeck.api.model.Formulario;
import com.echeck.api.model.Pergunta;
import com.echeck.api.model.Resposta;
import com.echeck.api.model.enums.TipoPergunta;
import com.echeck.api.repositories.FormularioRepository;
import com.echeck.api.repositories.PerguntaRepository;
import com.echeck.api.repositories.RespostaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RelatorioService {
    private final FormularioRepository formularioRepository;
    private final PerguntaRepository perguntaRepository;
    private final RespostaRepository respostaRepository;

    public RelatorioService(FormularioRepository formularioRepository,
                            PerguntaRepository perguntaRepository,
                            RespostaRepository respostaRepository) {
        this.formularioRepository = formularioRepository;
        this.perguntaRepository = perguntaRepository;
        this.respostaRepository = respostaRepository;
    }

    public RelatorioDto gerarRelatorio(Long formularioId) {
        Formulario formulario = formularioRepository.findById(formularioId)
                .orElseThrow(() -> new RuntimeException("Formulário não encontrado"));

        RelatorioDto relatorio = new RelatorioDto();
        relatorio.setNomeFormulario(formulario.getNome());
        relatorio.setItens(new ArrayList<>());

        List<Pergunta> perguntas = perguntaRepository.findByFormularioId(formularioId);

        for (Pergunta p : perguntas) {
            ItemRelatorioDto item = new ItemRelatorioDto();
            item.setPergunta(p.getDescricao());
            item.setTipo(p.getTipo());

            List<Resposta> respostas = respostaRepository.findByPergunta(p.getId());

            if (p.getTipo() == TipoPergunta.ESTRELAS) {
                if (!respostas.isEmpty()) {
                    double media = respostas.stream()
                            .filter(r -> r.getNota() != null)
                            .mapToInt(Resposta::getNota)
                            .average()
                            .orElse(0.0);
                    item.setMedia(Math.round(media * 10.0) / 10.0);
                } else {
                    item.setMedia(0.0);
                }

            } else if (p.getTipo() == TipoPergunta.TEXTO_ABERTO) {
                List<String> comentarios = respostas.stream()
                        .filter(r -> r.getTexto() != null && !r.getTexto().isEmpty())
                        .map(Resposta::getTexto)
                        .collect(Collectors.toList());
                item.setComentarios(comentarios);

            } else if (p.getTipo() == TipoPergunta.MULTIPLA_ESCOLHA) {
                Map<String, Long> contagem = new HashMap<>();
                if (p.getOpcoesRespostas() != null) {
                    p.getOpcoesRespostas().forEach(op -> contagem.put(op.getOpcao(), 0L));
                }
                for (Resposta r : respostas) {
                    if (r.getTexto() != null) {
                        String opcaoEscolhida = r.getTexto();
                        contagem.put(opcaoEscolhida, contagem.getOrDefault(opcaoEscolhida, 0L) + 1);
                    }
                }
                item.setContagemOpcoes(contagem);
            }
            relatorio.getItens().add(item);
        }

        if (!relatorio.getItens().isEmpty()) {
            long total = respostaRepository.findByPergunta(perguntas.get(0).getId()).size();
            relatorio.setTotalAvaliacoes(total);
        } else {
            relatorio.setTotalAvaliacoes(0L);
        }

        return relatorio;
    }
}
