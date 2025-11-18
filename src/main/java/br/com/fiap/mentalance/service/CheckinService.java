package br.com.fiap.mentalance.service;

import br.com.fiap.mentalance.dto.CheckinRequest;
import br.com.fiap.mentalance.dto.DashboardResumoDTO;
import br.com.fiap.mentalance.exception.NegocioException;
import br.com.fiap.mentalance.model.Checkin;
import br.com.fiap.mentalance.model.Usuario;
import br.com.fiap.mentalance.repository.CheckinRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckinService {

    private final CheckinRepository checkinRepository;
    private final OracleProcedureService oracleProcedureService;

    @Transactional
    public Checkin registrarCheckin(Usuario usuario, CheckinRequest dto, boolean usarProcedure) {
        if (usuario == null) {
            throw new NegocioException("Usuário não encontrado para registro de check-in.");
        }

        // Gera análise de sentimento e resposta baseado no texto
        String analiseSentimento = determinarSentimento(dto.getTexto());
        String respostaGerada = gerarResposta(dto.getTexto(), analiseSentimento);

        LocalDateTime agora = LocalDateTime.now();

        if (usarProcedure) {
            // Tenta usar sequence, se não existir usa MAX + 1
            Long nextId;
            try {
                nextId = oracleProcedureService.obterProximoIdSequence("CHECKIN_SEQ");
            } catch (Exception e) {
                nextId = checkinRepository.getNextId();
            }
            
            // Usa a procedure Oracle
            oracleProcedureService.inserirCheckin(
                    nextId, agora, dto.getEmocao(), dto.getTexto(), 
                    analiseSentimento, respostaGerada, usuario.getId()
            );
            // Busca o checkin recém-criado
            return checkinRepository.findById(nextId)
                    .orElseThrow(() -> new NegocioException("Erro ao criar check-in"));
        } else {
            // Usa JPA padrão (usa sequence automaticamente)
            Checkin checkin = new Checkin();
            checkin.setUsuario(usuario);
            checkin.setDataCheckin(agora);
            checkin.setEmocao(dto.getEmocao());
            checkin.setTexto(dto.getTexto());
            checkin.setAnaliseSentimento(analiseSentimento);
            checkin.setRespostaGerada(respostaGerada);
            return checkinRepository.save(checkin);
        }
    }

    private String determinarSentimento(String texto) {
        String textoLower = texto.toLowerCase();
        if (textoLower.contains("feliz") || textoLower.contains("bom") || textoLower.contains("ótimo") || 
            textoLower.contains("excelente") || textoLower.contains("bem")) {
            return "positivo";
        } else if (textoLower.contains("triste") || textoLower.contains("ruim") || 
                   textoLower.contains("estressado") || textoLower.contains("cansado") || 
                   textoLower.contains("ansioso")) {
            return "negativo";
        }
        return "neutro";
    }

    private String gerarResposta(String texto, String sentimento) {
        return switch (sentimento) {
            case "positivo" -> "Continue assim! Mantenha o foco e a energia positiva.";
            case "negativo" -> "Lembre-se de fazer pausas e cuidar de si mesmo. Você está fazendo o seu melhor.";
            default -> "Obrigado por compartilhar. Continue acompanhando seu bem-estar.";
        };
    }

    public List<Checkin> listarRecentes(Usuario usuario) {
        List<Checkin> todos = checkinRepository.findAllByUsuarioOrderByDataDesc(usuario);
        return todos.stream().limit(7).toList();
    }

    public List<Checkin> listarTodos(Usuario usuario) {
        return checkinRepository.findAllByUsuarioOrderByDataDesc(usuario);
    }

    public DashboardResumoDTO gerarResumoSemanal(Usuario usuario) {
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime inicioSemana = agora.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1)
                .withHour(0).withMinute(0).withSecond(0);

        List<Checkin> semana = checkinRepository.buscarPorPeriodo(usuario, inicioSemana, agora);

        if (semana.isEmpty()) {
            return DashboardResumoDTO.builder()
                    .totalCheckins(0)
                    .mediaEnergia(0)
                    .mediaSono(0)
                    .emocaoPredominante(null)
                    .distribuicaoHumor(Map.of())
                    .build();
        }

        Map<String, Long> contagemPorEmocao = semana.stream()
                .collect(Collectors.groupingBy(Checkin::getEmocao, Collectors.counting()));

        // Encontra a emoção predominante
        String emocaoPredominante = contagemPorEmocao.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        Map<String, Long> distribuicaoHumor = contagemPorEmocao;

        return DashboardResumoDTO.builder()
                .totalCheckins(semana.size())
                .mediaEnergia(0) // Não temos mais energia no novo modelo
                .mediaSono(0)   // Não temos mais sono no novo modelo
                .emocaoPredominante(emocaoPredominante)
                .distribuicaoHumor(distribuicaoHumor)
                .build();
    }

    public List<Checkin> listarPorPeriodo(Usuario usuario, LocalDateTime inicio, LocalDateTime fim) {
        return checkinRepository.buscarPorPeriodo(usuario, inicio, fim);
    }

    public long contarCheckins() {
        return checkinRepository.count();
    }

    public List<Checkin> listarRecentesGlobal() {
        List<Checkin> todos = checkinRepository.findTop10ByOrderByDataDesc();
        return todos.stream().limit(10).toList();
    }
}

