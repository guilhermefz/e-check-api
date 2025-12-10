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
    private final SendGridEmailService emailService; // 1. NOVO: Dependência de Email

    // 2. Construtor com Injeção de Dependência
    public ReservaService(ReservaRepository reservaRepository, UnidadeRepository unidadeRepository,
                          FormularioRepository formularioRepository, SendGridEmailService emailService){
        this.reservaRepository = reservaRepository;
        this.unidadeRepository = unidadeRepository;
        this.formularioRepository = formularioRepository;
        this.emailService = emailService; // Injeção correta
    }

    @Transactional
    public Reserva create(ReservaDto dto) {

        // 3. Validações
        if (dto.getUnidadeId() == null) {
            throw new IllegalArgumentException("O ID da Unidade é obrigatório para criar a reserva.");
        }
        if (!unidadeRepository.existsById(dto.getUnidadeId())) {
            throw new RuntimeException("Unidade não encontrada! ID: " + dto.getUnidadeId());
        }

        Reserva reserva = new Reserva();
        reserva.setToken(UUID.randomUUID().toString());

        // 4. Mapeamento de Campos (NOVA POSIÇÃO: início do método)
        reserva.setUnidade(dto.getUnidadeId());
        reserva.setCpf(dto.getCpf());
        reserva.setCnpj(dto.getCnpj());
        reserva.setTelefone(dto.getTelefone());
        reserva.setEmail(dto.getEmail());
        reserva.setDataCheckin(dto.getDataCheckin());
        reserva.setDataCheckout(dto.getDataCheckout());
        reserva.setStatus(dto.getStatus());


        // 5. Lógica de Encontrar Formulário (EXISTENTE)
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

        // 6. Salvar a Reserva
        Reserva reservaSalva = reservaRepository.save(reserva);

        // 7. NOVO: Enviar Email (Se houver email e formulário)
        if (reservaSalva.getEmail() != null && !reservaSalva.getEmail().isEmpty() && reservaSalva.getFormulario() != null) {
            enviarEmailFormulario(reservaSalva);
        }

        return reservaSalva;
    }

    // 8. NOVO: Método privado para envio de email
    private void enviarEmailFormulario(Reserva reserva) {
        // URL FIXA: Use o endereço do seu Frontend na AWS
        final String FRONTEND_BASE_URL = "http://44.201.180.209";

        String linkFormulario = FRONTEND_BASE_URL + "/responder-form/" + reserva.getToken();

        String assunto = "Sua Avaliação de Satisfação E-Check";
        String corpoEmail = "<h2>Olá!</h2><p>Por favor, complete a sua avaliação de satisfação clicando no link abaixo:</p>" +
                "<p><a href=\"" + linkFormulario + "\">Responder Formulário de Avaliação</a></p>" +
                "<p>Obrigado!</p>";

        try {
            emailService.sendEmail(reserva.getEmail(), assunto, corpoEmail);
        } catch (Exception e) {
            System.err.println("ERRO: Falha ao enviar e-mail para " + reserva.getEmail() + ": " + e.getMessage());
        }
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

    public void enviarEmailReserva(Long reservaId, String emailDestino, String linkCompleto) {

        // 1. Validação/Busca da Reserva (para garantir que o ID é válido)
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada para o ID fornecido: " + reservaId));

        // 2. Montar o Email com o link e email fornecidos pelo Front
        String assunto = "Reenvio de Formulário de Satisfação (Reserva ID: " + reservaId + ")";

        // O corpo usa o link já montado pelo Front
        String corpoEmail = "<h2>Olá!</h2>" +
                "<p>Estamos reenviando o link de avaliação da sua reserva (ID: " + reservaId + ").</p>" +
                "<p>Por favor, clique no link abaixo:</p>" +
                "<p><a href=\"" + linkCompleto + "\">Responder Formulário de Avaliação</a></p>" +
                "<p>Obrigado!</p>";

        // 3. Enviar
        try {
            emailService.sendEmail(emailDestino, assunto, corpoEmail); // Usa o email de destino
        } catch (Exception e) {
            System.err.println("ERRO: Falha ao enviar e-mail para " + emailDestino + " para a reserva " + reservaId + ": " + e.getMessage());
            throw new RuntimeException("Falha na comunicação com o serviço de email. Verifique a chave SendGrid e tente novamente.");
        }
    }

}