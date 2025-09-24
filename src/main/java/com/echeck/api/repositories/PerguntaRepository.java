package com.echeck.api.repositories;

import jakarta.persistence.metamodel.SingularAttribute;
import com.echeck.api.model.Pergunta;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public interface PerguntaRepository extends JpaRepository<Pergunta, Long>
{
    List<Pergunta> findByDescricaoContainingIgnoreCase(String descricao);
}
