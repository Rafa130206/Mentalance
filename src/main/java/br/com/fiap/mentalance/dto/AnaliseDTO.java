package br.com.fiap.mentalance.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class AnaliseDTO {
    Long checkinId;
    LocalDate data;
    String resumo;
    String recomendacoes;
    String sentimentos;
}

