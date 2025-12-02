package com.echeck.api.services;

import com.echeck.api.Dtos.ItemRelatorioDto;
import com.echeck.api.Dtos.RelatorioDto;
import com.echeck.api.model.enums.TipoPergunta;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

@Service
public class PdfService {
    public ByteArrayInputStream gerarPdfRelatorio(RelatorioDto relatorio) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph titulo = new Paragraph("Relatório de Satisfação", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(new Paragraph("\n")); // Pula linha

            Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA, 12);
            document.add(new Paragraph("Formulário: " + relatorio.getNomeFormulario(), fontSubtitulo));
            document.add(new Paragraph("Total de Avaliações: " + relatorio.getTotalAvaliacoes(), fontSubtitulo));
            document.add(new Paragraph("-----------------------------------------------------------------------"));
            document.add(new Paragraph("\n"));

            Font fontPergunta = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font fontTexto = FontFactory.getFont(FontFactory.HELVETICA, 11);

            for (ItemRelatorioDto item : relatorio.getItens()) {
                document.add(new Paragraph("Pergunta: " + item.getPergunta(), fontPergunta));
                document.add(new Paragraph("Tipo: " + item.getTipo(), fontTexto));

                if (item.getTipo() == TipoPergunta.ESTRELAS) {
                    document.add(new Paragraph("Média: " + item.getMedia() + " / 5.0", fontTexto));

                } else if (item.getTipo() == TipoPergunta.TEXTO_ABERTO) {
                    document.add(new Paragraph("Comentários:", fontTexto));
                    if (item.getComentarios().isEmpty()) {
                        document.add(new Paragraph("   (Nenhum comentário)", fontTexto));
                    } else {
                        for (String comentario : item.getComentarios()) {
                            document.add(new Paragraph("   - " + comentario, fontTexto));
                        }
                    }
                } else if (item.getTipo() == TipoPergunta.MULTIPLA_ESCOLHA) {
                    document.add(new Paragraph("Votos:", fontTexto));
                    for (Map.Entry<String, Long> entry : item.getContagemOpcoes().entrySet()) {
                        document.add(new Paragraph("   [ " + entry.getKey() + " ]: " + entry.getValue() + " votos", fontTexto));
                    }
                }
                document.add(new Paragraph("\n"));
            }
            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF: " + e.getMessage());
        }
        return new ByteArrayInputStream(out.toByteArray());
    }
}
