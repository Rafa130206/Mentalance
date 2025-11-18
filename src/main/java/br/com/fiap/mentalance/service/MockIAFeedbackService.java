package br.com.fiap.mentalance.service;

import br.com.fiap.mentalance.model.Analise;
import br.com.fiap.mentalance.model.Checkin;
import org.springframework.stereotype.Service;

/**
 * Serviço mockado para geração de análises.
 * NOTA: Este serviço não é mais usado no novo modelo.
 * A análise de sentimento e resposta são geradas diretamente no CheckinService.
 * Mantido para compatibilidade, mas pode ser removido se não for necessário.
 */
@Service
public class MockIAFeedbackService implements IAFeedbackService {

    @Override
    public Analise gerarAnalise(Checkin checkin) {
        // Este método não é mais usado - a análise é gerada no CheckinService
        Analise analise = new Analise();
        analise.setResumo("Análise gerada automaticamente no check-in.");
        analise.setRecomendacoes("Continue acompanhando seu bem-estar.");
        analise.setSentimentos(checkin.getAnaliseSentimento());
        return analise;
    }
}

