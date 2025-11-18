package br.com.fiap.mentalance.repository;

import br.com.fiap.mentalance.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT COALESCE(MAX(u.id), 0) + 1 FROM Usuario u")
    Long getNextId();
}

