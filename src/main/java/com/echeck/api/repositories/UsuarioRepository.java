package com.echeck.api.repositories;

import com.echeck.api.model.Usuario;
import jakarta.persistence.Id;
import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID>, Serializable {
    void deleteById(SingularAttribute<AbstractPersistable, Serializable> id);

    List<Usuario> findByNomeContainingIgnoreCase(String nome);

    Optional<Usuario> findById(long id);
}








