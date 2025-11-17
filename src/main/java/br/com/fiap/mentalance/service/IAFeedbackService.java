package br.com.fiap.mentalance.service;

import br.com.fiap.mentalance.model.Analise;
import br.com.fiap.mentalance.model.Checkin;

public interface IAFeedbackService {

    Analise gerarAnalise(Checkin checkin);
}

