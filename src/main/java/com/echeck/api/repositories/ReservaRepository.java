package com.echeck.api.repositories;

import com.echeck.api.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByUnidade(long unidadeId);

    Optional<Reserva> findByToken(String token);
}
