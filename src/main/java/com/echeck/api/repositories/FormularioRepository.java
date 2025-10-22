package com.echeck.api.repositories;

import com.echeck.api.model.Formulario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormularioRepository extends JpaRepository<Formulario, Long> {

    List<Formulario> findByUnidadeId(Long unidadeId);
}
