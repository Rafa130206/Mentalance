package br.com.fiap.mentalance.service;

import br.com.fiap.mentalance.model.Analise;
import br.com.fiap.mentalance.model.Checkin;
import br.com.fiap.mentalance.model.EstadoHumor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MockIAFeedbackService implements IAFeedbackService {

    private static final Map<EstadoHumor, String> RESUMOS_PADRAO = Map.of(
            EstadoHumor.MUITO_BEM, "Você demonstrou um alto nível de bem-estar hoje. Continue reforçando seus hábitos positivos.",
            EstadoHumor.BEM, "Seu humor está equilibrado. Aproveite para celebrar pequenas vitórias do dia.",
            EstadoHumor.NEUTRO, "Um dia neutro é normal. Que tal reservar um tempo para algo prazeroso?",
            EstadoHumor.CANSADO, "Seu corpo pede descanso. Procure desacelerar e priorizar o sono reparador.",
            EstadoHumor.ESTRESSADO, "Níveis altos de estresse identificados. Respirações profundas e pausas podem ajudar."
    );

    @Override
    public Analise gerarAnalise(Checkin checkin) {
        Analise analise = new Analise();
        analise.setCheckin(checkin);
        analise.setModelo("mock-local");
        analise.setResumo(criarResumo(checkin));
        analise.setRecomendacoes(criarRecomendacoes(checkin));
        analise.setSentimentos(checkin.getHumor().name());
        return analise;
    }

    private String criarResumo(Checkin checkin) {
        return RESUMOS_PADRAO.getOrDefault(checkin.getHumor(),
                "Check-in registrado. Continue acompanhado sua evolução emocional.");
    }

    private String criarRecomendacoes(Checkin checkin) {
        String contexto = checkin.getContexto();
        return switch (checkin.getHumor()) {
            case MUITO_BEM, BEM -> "Mantenha as práticas que têm feito bem a você. Considere registrar o que funcionou: " + contexto;
            case NEUTRO -> "Busque pequenas ações que tragam satisfação. Experimente algo novo e leve: " + contexto;
            case CANSADO -> "Reorganize sua agenda para priorizar descanso. Hidratação e breves pausas podem ajudar. Contexto: " + contexto;
            case ESTRESSADO -> "Que tal separar 5 minutos para respirar profundamente? Compartilhe com alguém de confiança. Contexto: " + contexto;
        };
    }
}

