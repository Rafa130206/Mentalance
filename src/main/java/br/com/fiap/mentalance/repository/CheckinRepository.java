package br.com.fiap.mentalance.repository;

import br.com.fiap.mentalance.model.Checkin;
import br.com.fiap.mentalance.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CheckinRepository extends JpaRepository<Checkin, Long> {

    List<Checkin> findByUsuarioOrderByDataCheckinDesc(Usuario usuario);

    @Query("SELECT c FROM Checkin c WHERE c.usuario = :usuario ORDER BY c.dataCheckin DESC")
    List<Checkin> findTop7ByUsuarioOrderByDataDesc(Usuario usuario);

    @Query("SELECT c FROM Checkin c WHERE c.usuario = :usuario ORDER BY c.dataCheckin DESC")
    List<Checkin> findAllByUsuarioOrderByDataDesc(Usuario usuario);

    @Query("SELECT c FROM Checkin c ORDER BY c.dataCheckin DESC")
    List<Checkin> findTop10ByOrderByDataDesc();

    @Query("SELECT c FROM Checkin c WHERE c.usuario = :usuario AND c.dataCheckin BETWEEN :inicio AND :fim ORDER BY c.dataCheckin")
    List<Checkin> buscarPorPeriodo(Usuario usuario, LocalDateTime inicio, LocalDateTime fim);

    @Query("SELECT c.emocao, COUNT(c) FROM Checkin c WHERE c.usuario = :usuario AND c.dataCheckin BETWEEN :inicio AND :fim GROUP BY c.emocao")
    List<Object[]> contarPorEmocao(Usuario usuario, LocalDateTime inicio, LocalDateTime fim);

    @Query("SELECT COALESCE(MAX(c.id), 0) + 1 FROM Checkin c")
    Long getNextId();
}

