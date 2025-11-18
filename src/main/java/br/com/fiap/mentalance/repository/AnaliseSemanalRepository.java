package br.com.fiap.mentalance.repository;

import br.com.fiap.mentalance.model.AnaliseSemanal;
import br.com.fiap.mentalance.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AnaliseSemanalRepository extends JpaRepository<AnaliseSemanal, Long> {

    List<AnaliseSemanal> findByUsuario(Usuario usuario);

    Optional<AnaliseSemanal> findByUsuarioAndSemanaReferencia(Usuario usuario, String semanaReferencia);

    @Query("SELECT COALESCE(MAX(a.id), 0) + 1 FROM AnaliseSemanal a")
    Long getNextId();
}

