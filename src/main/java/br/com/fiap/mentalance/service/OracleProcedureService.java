package br.com.fiap.mentalance.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Clob;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OracleProcedureService {

    private final EntityManager entityManager;

    /**
     * Chama a procedure inserts.inserir_usuario
     */
    @Transactional
    public void inserirUsuario(Long idUsuario, String nome, String email, String senha, String cargo) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("inserts.inserir_usuario");
        
        query.registerStoredProcedureParameter("p_ID_USUARIO", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_NOME", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_EMAIL", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_SENHA", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_CARGO", String.class, ParameterMode.IN);
        
        query.setParameter("p_ID_USUARIO", idUsuario);
        query.setParameter("p_NOME", nome);
        query.setParameter("p_EMAIL", email);
        query.setParameter("p_SENHA", senha);
        query.setParameter("p_CARGO", cargo);
        
        query.execute();
    }

    /**
     * Chama a procedure inserts.inserir_checkin
     */
    @Transactional
    public void inserirCheckin(Long idCheckin, LocalDateTime dataCheckin, String emocao, 
                               String texto, String analiseSentimento, String respostaGerada, 
                               Long usuarioId) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("inserts.inserir_checkin");
        
        query.registerStoredProcedureParameter("p_ID_CHECKIN", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_DATA_CHECKIN", java.sql.Timestamp.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_EMOCAO", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_TEXTO", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_analise_sent", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_resposta", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_ID_USUARIO", Long.class, ParameterMode.IN);
        
        query.setParameter("p_ID_CHECKIN", idCheckin);
        query.setParameter("p_DATA_CHECKIN", java.sql.Timestamp.valueOf(dataCheckin));
        query.setParameter("p_EMOCAO", emocao);
        query.setParameter("p_TEXTO", texto);
        query.setParameter("p_analise_sent", analiseSentimento);
        query.setParameter("p_resposta", respostaGerada);
        query.setParameter("p_ID_USUARIO", usuarioId);
        
        query.execute();
    }

    /**
     * Chama a procedure inserts.inserir_analise_semanal
     */
    @Transactional
    public void inserirAnaliseSemanal(Long idAnalise, String semanaReferencia, String emocaoPredominante,
                                      String resumo, String recomendacao, Long usuarioId) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("inserts.inserir_analise_semanal");
        
        query.registerStoredProcedureParameter("p_ID_ANALISE", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_semana_ref", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_EMOCAO_pred", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_RESUMO", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_RECOMENDACAO", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_ID_USUARIO", Long.class, ParameterMode.IN);
        
        query.setParameter("p_ID_ANALISE", idAnalise);
        query.setParameter("p_semana_ref", semanaReferencia);
        query.setParameter("p_EMOCAO_pred", emocaoPredominante);
        query.setParameter("p_RESUMO", resumo);
        query.setParameter("p_RECOMENDACAO", recomendacao);
        query.setParameter("p_ID_USUARIO", usuarioId);
        
        query.execute();
    }

    /**
     * Chama a procedure pkg_exportacao_json.pr_exportar_dataset_json
     * Retorna o JSON como String
     * Limpa o cache do Hibernate antes de chamar para garantir que veja todos os dados
     */
    @Transactional(readOnly = true)
    public String exportarDatasetJson() {
        try {
            // Limpa o cache do EntityManager para garantir que veja dados atualizados
            entityManager.clear();
            entityManager.flush();
            
            // Tenta usar a procedure primeiro
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("pkg_exportacao_json.pr_exportar_dataset_json");
            
            query.registerStoredProcedureParameter("p_json", Clob.class, ParameterMode.OUT);
            
            query.execute();
            
            Clob clob = (Clob) query.getOutputParameterValue("p_json");
            
            if (clob != null) {
                long length = clob.length();
                if (length > 0) {
                    String json = clob.getSubString(1, (int) length);
                    // A procedure sempre retorna o JSON completo, então retorna diretamente
                    return json;
                }
            }
            
            // Se chegou aqui, a procedure não retornou dados válidos
            System.err.println("Procedure retornou CLOB vazio ou null");
            return exportarDatasetJsonAlternativo();
        } catch (Exception e) {
            // Se a procedure falhar, usa método alternativo
            System.err.println("Erro ao chamar procedure, usando método alternativo: " + e.getMessage());
            e.printStackTrace();
            return exportarDatasetJsonAlternativo();
        }
    }
    
    /**
     * Método alternativo que busca todos os dados diretamente das tabelas
     * e monta o JSON manualmente, garantindo que todos os dados sejam incluídos
     * Usa query nativa sem cache para garantir que veja todos os dados do banco
     */
    @Transactional(readOnly = true)
    private String exportarDatasetJsonAlternativo() {
        try {
            // Limpa cache antes de buscar
            entityManager.clear();
            
            StringBuilder json = new StringBuilder();
            json.append("{\n");
            
            // Busca todos os usuários - query nativa direta sem cache
            String usuariosSql = "SELECT ID_USUARIO, NOME, EMAIL, CARGO, DATA_CADASTRO FROM USUARIO ORDER BY ID_USUARIO";
            @SuppressWarnings("unchecked")
            List<Object[]> usuarios = (List<Object[]>) entityManager.createNativeQuery(usuariosSql)
                    .setHint("javax.persistence.cache.storeMode", "BYPASS")
                    .getResultList();
            
            json.append("  \"usuarios\": [\n");
            for (int i = 0; i < usuarios.size(); i++) {
                Object[] row = usuarios.get(i);
                json.append("    {\n");
                json.append("      \"id\": ").append(row[0]).append(",\n");
                json.append("      \"nome\": \"").append(escapeJson(row[1])).append("\",\n");
                json.append("      \"email\": \"").append(escapeJson(row[2])).append("\",\n");
                json.append("      \"cargo\": \"").append(escapeJson(row[3])).append("\",\n");
                json.append("      \"dataCadastro\": \"").append(row[4]).append("\"\n");
                json.append("    }");
                if (i < usuarios.size() - 1) json.append(",");
                json.append("\n");
            }
            json.append("  ],\n");
            
            // Busca todos os check-ins - query nativa direta sem cache
            String checkinsSql = "SELECT ID_CHECKIN, DATA_CHECKIN, EMOCAO, TEXTO, ANALISE_SENTIMENTO, RESPOSTA_GERADA, USUARIO_ID_USUARIO FROM CHECKIN ORDER BY ID_CHECKIN";
            @SuppressWarnings("unchecked")
            List<Object[]> checkins = (List<Object[]>) entityManager.createNativeQuery(checkinsSql)
                    .setHint("javax.persistence.cache.storeMode", "BYPASS")
                    .getResultList();
            
            json.append("  \"checkins\": [\n");
            for (int i = 0; i < checkins.size(); i++) {
                Object[] row = checkins.get(i);
                json.append("    {\n");
                json.append("      \"id\": ").append(row[0]).append(",\n");
                json.append("      \"dataCheckin\": \"").append(row[1]).append("\",\n");
                json.append("      \"emocao\": \"").append(escapeJson(row[2])).append("\",\n");
                json.append("      \"texto\": \"").append(escapeJson(row[3])).append("\",\n");
                json.append("      \"analiseSentimento\": \"").append(escapeJson(row[4])).append("\",\n");
                json.append("      \"respostaGerada\": \"").append(escapeJson(row[5])).append("\",\n");
                json.append("      \"usuarioId\": ").append(row[6]).append("\n");
                json.append("    }");
                if (i < checkins.size() - 1) json.append(",");
                json.append("\n");
            }
            json.append("  ],\n");
            
            // Busca todas as análises semanais - query nativa direta sem cache
            String analisesSql = "SELECT ID_ANALISE, SEMANA_REFERENCIA, EMOCAO_PREDOMINANTE, RESUMO, RECOMENDACAO, USUARIO_ID_USUARIO FROM ANALISE_SEMANAL ORDER BY ID_ANALISE";
            @SuppressWarnings("unchecked")
            List<Object[]> analises = (List<Object[]>) entityManager.createNativeQuery(analisesSql)
                    .setHint("javax.persistence.cache.storeMode", "BYPASS")
                    .getResultList();
            
            json.append("  \"analises_semanais\": [\n");
            for (int i = 0; i < analises.size(); i++) {
                Object[] row = analises.get(i);
                json.append("    {\n");
                json.append("      \"id\": ").append(row[0]).append(",\n");
                json.append("      \"semanaReferencia\": \"").append(escapeJson(row[1])).append("\",\n");
                json.append("      \"emocaoPredominante\": \"").append(escapeJson(row[2])).append("\",\n");
                json.append("      \"resumo\": \"").append(escapeJson(row[3])).append("\",\n");
                json.append("      \"recomendacao\": \"").append(escapeJson(row[4])).append("\",\n");
                json.append("      \"usuarioId\": ").append(row[5]).append("\n");
                json.append("    }");
                if (i < analises.size() - 1) json.append(",");
                json.append("\n");
            }
            json.append("  ]\n");
            json.append("}");
            
            return json.toString();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao exportar dataset JSON: " + e.getMessage(), e);
        }
    }
    
    /**
     * Escapa caracteres especiais para JSON
     */
    private String escapeJson(Object value) {
        if (value == null) return "";
        String str = value.toString();
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    /**
     * Chama a função pkg_funcoes_json.fn_dataset_completo_json
     * Retorna o JSON como String
     */
    @Transactional(readOnly = true)
    public String obterDatasetCompletoJson() {
        try {
            // Para funções, usamos uma query nativa
            String sql = "SELECT pkg_funcoes_json.fn_dataset_completo_json FROM DUAL";
            Object result = entityManager.createNativeQuery(sql).getSingleResult();
            
            if (result instanceof Clob) {
                Clob clob = (Clob) result;
                long length = clob.length();
                if (length > 0) {
                    return clob.getSubString(1, (int) length);
                }
            } else if (result instanceof String) {
                return (String) result;
            }
            
            return "{}";
        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter dataset JSON", e);
        }
    }

    /**
     * Obtém o próximo ID de uma sequence
     */
    public Long obterProximoIdSequence(String sequenceName) {
        String sql = "SELECT " + sequenceName + ".NEXTVAL FROM DUAL";
        Object result = entityManager.createNativeQuery(sql).getSingleResult();
        return ((Number) result).longValue();
    }
}

