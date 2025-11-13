package com.echeck.api.repositories;

import com.echeck.api.model.OpcaoResposta;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpcaoRespostaRepository extends JpaRepository<OpcaoResposta, Long> {

    List<OpcaoResposta> findByPerguntaId(Long perguntaId);
}
