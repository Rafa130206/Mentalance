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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ANALISE_SEMANAL")
public class AnaliseSemanal {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "analise_semanal_seq")
    @SequenceGenerator(name = "analise_semanal_seq", sequenceName = "ANALISE_SEMANAL_SEQ", allocationSize = 1)
    @Column(name = "ID_ANALISE")
    private Long id;

    @NotBlank
    @Column(name = "SEMANA_REFERENCIA", nullable = false, length = 100)
    private String semanaReferencia;

    @NotBlank
    @Column(name = "EMOCAO_PREDOMINANTE", nullable = false, length = 100)
    private String emocaoPredominante;

    @NotBlank
    @Column(name = "RESUMO", nullable = false, length = 2000)
    private String resumo;

    @NotBlank
    @Column(name = "RECOMENDACAO", nullable = false, length = 2000)
    private String recomendacao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USUARIO_ID_USUARIO", nullable = false)
    private Usuario usuario;
}

