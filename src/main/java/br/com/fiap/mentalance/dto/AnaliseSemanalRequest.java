package br.com.fiap.mentalance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AnaliseSemanalRequest {

    @NotBlank
    @Pattern(regexp = "^[0-9]{4}-W[0-9]{2}$", message = "Formato inválido. Use YYYY-WNN (ex: 2025-W01)")
    private String semanaReferencia;

    @NotBlank
    @Size(max = 100)
    private String emocaoPredominante;

    @NotBlank
    @Size(max = 2000)
    private String resumo;

    @NotBlank
    @Size(max = 2000)
    private String recomendacao;
}

