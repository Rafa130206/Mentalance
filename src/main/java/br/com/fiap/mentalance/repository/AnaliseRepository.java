package br.com.fiap.mentalance.repository;

import br.com.fiap.mentalance.model.Analise;
import br.com.fiap.mentalance.model.Checkin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnaliseRepository extends JpaRepository<Analise, Long> {

    Optional<Analise> findByCheckin(Checkin checkin);
}

