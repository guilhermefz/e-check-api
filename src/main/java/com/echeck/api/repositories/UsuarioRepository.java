package com.echeck.api.repositories;

import com.echeck.api.model.Usuario;
import jakarta.persistence.Id;
import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Id> {
    void deleteById(SingularAttribute<AbstractPersistable, Serializable> id);

    List<Usuario> findByNomeContainingIgnoreCase(String nome);
}








