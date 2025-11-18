package br.com.fiap.mentalance.service;

import br.com.fiap.mentalance.dto.AnaliseSemanalRequest;
import br.com.fiap.mentalance.exception.NegocioException;
import br.com.fiap.mentalance.model.AnaliseSemanal;
import br.com.fiap.mentalance.model.Usuario;
import br.com.fiap.mentalance.repository.AnaliseSemanalRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnaliseSemanalService {

    private final AnaliseSemanalRepository analiseSemanalRepository;
    private final OracleProcedureService oracleProcedureService;

    @Transactional
    public AnaliseSemanal inserirAnaliseSemanal(Usuario usuario, AnaliseSemanalRequest dto, boolean usarProcedure) {
        if (usuario == null) {
            throw new NegocioException("Usuário não encontrado.");
        }

        if (usarProcedure) {
            // Tenta usar sequence, se não existir usa MAX + 1
            Long nextId;
            try {
                nextId = oracleProcedureService.obterProximoIdSequence("ANALISE_SEMANAL_SEQ");
            } catch (Exception e) {
                nextId = analiseSemanalRepository.getNextId();
            }

            // Usa a procedure Oracle
            oracleProcedureService.inserirAnaliseSemanal(
                    nextId, dto.getSemanaReferencia(), dto.getEmocaoPredominante(),
                    dto.getResumo(), dto.getRecomendacao(), usuario.getId()
            );

            // Busca a análise recém-criada
            return analiseSemanalRepository.findById(nextId)
                    .orElseThrow(() -> new NegocioException("Erro ao criar análise semanal"));
        } else {
            // Usa JPA padrão (usa sequence automaticamente)
            AnaliseSemanal analise = new AnaliseSemanal();
            analise.setUsuario(usuario);
            analise.setSemanaReferencia(dto.getSemanaReferencia());
            analise.setEmocaoPredominante(dto.getEmocaoPredominante());
            analise.setResumo(dto.getResumo());
            analise.setRecomendacao(dto.getRecomendacao());
            return analiseSemanalRepository.save(analise);
        }
    }
}

