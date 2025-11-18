package br.com.fiap.mentalance.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "USUARIO")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
    @SequenceGenerator(name = "usuario_seq", sequenceName = "USUARIO_SEQ", allocationSize = 1)
    @Column(name = "ID_USUARIO")
    private Long id;

    @NotBlank
    @Size(min = 3, max = 100)
    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @NotBlank
    @Email
    @Column(name = "EMAIL", unique = true, nullable = false, length = 100)
    private String email;

    @NotBlank
    @Size(min = 6, max = 2000)
    @Column(name = "SENHA", nullable = false, length = 2000)
    private String senha;

    @Size(max = 100)
    @Column(name = "CARGO", nullable = false, length = 100)
    private String cargo;

    @Column(name = "DATA_CADASTRO", nullable = false)
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Checkin> checkins = new ArrayList<>();
}

