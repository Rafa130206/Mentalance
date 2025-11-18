package br.com.fiap.mentalance.dto;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class DashboardResumoDTO {
    long totalCheckins;
    String emocaoPredominante; // Agora usa String em vez de enum
    double mediaEnergia;
    double mediaSono;
    Map<String, Long> distribuicaoHumor;
}

