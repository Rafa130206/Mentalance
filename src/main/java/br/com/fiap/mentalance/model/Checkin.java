package br.com.fiap.mentalance.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "CHECKIN")
public class Checkin {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "checkin_seq")
    @SequenceGenerator(name = "checkin_seq", sequenceName = "CHECKIN_SEQ", allocationSize = 1)
    @Column(name = "ID_CHECKIN")
    private Long id;

    @NotNull
    @Column(name = "DATA_CHECKIN", nullable = false)
    private LocalDateTime dataCheckin = LocalDateTime.now();

    @NotBlank
    @Column(name = "EMOCAO", nullable = false, length = 100)
    private String emocao;

    @NotBlank
    @Column(name = "TEXTO", nullable = false, length = 100)
    private String texto;

    @NotBlank
    @Column(name = "ANALISE_SENTIMENTO", nullable = false, length = 100)
    private String analiseSentimento;

    @NotBlank
    @Column(name = "RESPOSTA_GERADA", nullable = false, length = 2000)
    private String respostaGerada;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USUARIO_ID_USUARIO", nullable = false)
    private Usuario usuario;
}

