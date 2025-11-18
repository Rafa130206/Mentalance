# Changelog - Adaptação para Oracle Database

## Resumo das Alterações

Este documento descreve todas as alterações realizadas para adaptar o projeto Mentalance ao banco de dados Oracle conforme o script SQL fornecido.

## 1. Modelos Atualizados

### Usuario
- ✅ Removido campo `username`
- ✅ Adicionado campo `cargo` (VARCHAR2(100))
- ✅ Tabela renomeada para `USUARIO` (maiúsculas)
- ✅ Colunas mapeadas para nomes Oracle: `ID_USUARIO`, `NOME`, `EMAIL`, `SENHA`, `CARGO`, `DATA_CADASTRO`
- ✅ Configurado para usar sequence `USUARIO_SEQ`

### Checkin
- ✅ Estrutura completamente reformulada para corresponder à tabela `CHECKIN` do Oracle
- ✅ Campos atualizados:
  - `ID_CHECKIN` (NUMBER)
  - `DATA_CHECKIN` (TIMESTAMP)
  - `EMOCAO` (VARCHAR2(100)) - substitui `humor` (enum)
  - `TEXTO` (VARCHAR2(100)) - substitui `contexto`
  - `ANALISE_SENTIMENTO` (VARCHAR2(100)) - novo campo
  - `RESPOSTA_GERADA` (VARCHAR2(2000)) - novo campo
  - `USUARIO_ID_USUARIO` (NUMBER) - foreign key
- ✅ Removidos campos: `energia`, `sono`, `humor` (enum), `contexto`
- ✅ Configurado para usar sequence `CHECKIN_SEQ`

### AnaliseSemanal (Novo)
- ✅ Criado novo modelo `AnaliseSemanal` correspondente à tabela `ANALISE_SEMANAL`
- ✅ Campos:
  - `ID_ANALISE` (NUMBER)
  - `SEMANA_REFERENCIA` (VARCHAR2(100)) - formato: YYYY-WNN
  - `EMOCAO_PREDOMINANTE` (VARCHAR2(100))
  - `RESUMO` (VARCHAR2(2000))
  - `RECOMENDACAO` (VARCHAR2(2000))
  - `USUARIO_ID_USUARIO` (NUMBER) - foreign key
- ✅ Configurado para usar sequence `ANALISE_SEMANAL_SEQ`

## 2. Repositories Atualizados

### UsuarioRepository
- ✅ Removido `findByUsername()` e `existsByUsername()`
- ✅ Adicionado método `getNextId()` para obter próximo ID

### CheckinRepository
- ✅ Queries atualizadas para usar `dataCheckin` em vez de `data`
- ✅ Queries atualizadas para usar `emocao` em vez de `humor`
- ✅ Método `buscarPorPeriodo()` atualizado para usar `LocalDateTime`
- ✅ Adicionado método `getNextId()`

### AnaliseSemanalRepository (Novo)
- ✅ Criado repository completo com métodos de busca
- ✅ Adicionado método `getNextId()`

## 3. Serviços Criados/Atualizados

### OracleProcedureService (Novo)
- ✅ Serviço completo para chamar procedures Oracle:
  - `inserirUsuario()` - chama `inserts.inserir_usuario`
  - `inserirCheckin()` - chama `inserts.inserir_checkin`
  - `inserirAnaliseSemanal()` - chama `inserts.inserir_analise_semanal`
  - `exportarDatasetJson()` - chama `pkg_exportacao_json.pr_exportar_dataset_json`
  - `obterDatasetCompletoJson()` - chama `pkg_funcoes_json.fn_dataset_completo_json`
  - `obterProximoIdSequence()` - obtém próximo ID de uma sequence

### UsuarioService
- ✅ Atualizado para usar email em vez de username
- ✅ Adicionado suporte para usar procedures Oracle (parâmetro `usarProcedure`)
- ✅ Integrado com `OracleProcedureService`
- ✅ Removidas referências a `username` e `perfil`

### CheckinService
- ✅ Completamente reformulado para novo modelo de Checkin
- ✅ Adicionada lógica para gerar `analiseSentimento` e `respostaGerada` automaticamente
- ✅ Adicionado suporte para usar procedures Oracle
- ✅ Atualizado `gerarResumoSemanal()` para trabalhar com emoções (strings) em vez de enum
- ✅ Removidos métodos que dependiam de `energia` e `sono`

### SessaoUsuarioService
- ✅ Atualizado para usar email em vez de username na autenticação

### UsuarioDetailsService
- ✅ Atualizado para usar email como username no Spring Security
- ✅ Simplificada lógica de roles (baseada em email do admin)

## 4. DTOs Atualizados

### UsuarioRegistroRequest
- ✅ Removido campo `username`
- ✅ Adicionado campo `cargo` (opcional)
- ✅ Tamanhos de validação ajustados para corresponder ao schema Oracle

### CheckinRequest
- ✅ Simplificado para apenas `emocao` e `texto`
- ✅ Removidos campos: `humor`, `energia`, `sono`, `contexto`

## 5. Controllers Atualizados

### CheckinController
- ✅ Removidas referências a `EstadoHumor` enum
- ✅ Atualizado para usar novo `CheckinRequest`
- ✅ Adicionado parâmetro para escolher entre procedure e JPA

### DashboardController
- ✅ Removida referência a `listarAnalises()` (método removido)

### AdminController
- ✅ Removidas referências a `mediaEnergia()` e `mediaSono()` (métodos removidos)

### OracleProcedureController (Novo)
- ✅ Controller REST para expor endpoints de exportação JSON:
  - `GET /api/oracle/exportar-json` - usa procedure
  - `GET /api/oracle/dataset-json` - usa função

## 6. Configuração

### application.properties
- ✅ Configurado para Oracle Database
- ✅ Dialeto Hibernate ajustado para `OracleDialect`
- ✅ `ddl-auto` definido como `none` (tabelas criadas via script SQL)
- ✅ Logging SQL habilitado para debug

### DataLoader
- ✅ Atualizado para não usar `username`
- ✅ Adicionado campo `cargo` no usuário admin

## 7. Arquivos SQL Criados

### oracle-sequences.sql
- ✅ Script para criar sequences necessárias:
  - `USUARIO_SEQ`
  - `CHECKIN_SEQ`
  - `ANALISE_SEMANAL_SEQ`

## 8. Documentação

### ORACLE_SETUP.md
- ✅ Guia completo de configuração do Oracle
- ✅ Documentação de todas as procedures disponíveis
- ✅ Instruções de uso das procedures no backend
- ✅ Estrutura das tabelas

## Notas Importantes

1. **Autenticação**: O sistema agora usa EMAIL em vez de USERNAME. Atualize os templates HTML se necessário.

2. **Sequences**: Execute o script `oracle-sequences.sql` após criar as tabelas principais.

3. **Procedures vs JPA**: Os serviços permitem escolher entre usar procedures Oracle ou JPA padrão através do parâmetro `usarProcedure`.

4. **Validações**: As procedures Oracle incluem validações (email, senha, semana, etc.). Erros são lançados como exceções Oracle.

5. **Auditoria**: Todas as operações são automaticamente auditadas através de triggers definidas no script SQL.

6. **Modelo Antigo**: O modelo `Analise` antigo ainda existe no código mas não é mais usado. Pode ser removido se não for necessário.

## Próximos Passos Sugeridos

1. Atualizar templates HTML para refletir as mudanças nos formulários (remover campos de energia/sono, adicionar campo de emoção)
2. Testar todas as procedures Oracle
3. Verificar se as sequences estão funcionando corretamente
4. Atualizar testes unitários se existirem
5. Remover código não utilizado (modelo Analise antigo, enum EstadoHumor se não for mais usado)

