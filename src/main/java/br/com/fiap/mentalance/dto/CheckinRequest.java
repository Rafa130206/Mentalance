package br.com.fiap.mentalance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CheckinRequest {

    @NotBlank
    @Size(max = 100)
    private String emocao;

    @NotBlank
    @Size(max = 100)
    private String texto;
}

