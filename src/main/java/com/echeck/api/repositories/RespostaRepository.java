package com.echeck.api.repositories;

import com.echeck.api.model.Resposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RespostaRepository extends JpaRepository<Resposta, Long> {

    List<Resposta> findByReserva(long reservaId);

    List<Resposta> findByPergunta(long perguntaId);
}
