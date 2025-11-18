# Configuração Oracle para Mentalance

## Pré-requisitos

1. Oracle Database instalado e rodando
2. Execute o script `mentalance.sql` fornecido para criar as tabelas, procedures e funções
3. Execute o script `src/main/resources/db/oracle-sequences.sql` para criar as sequences necessárias

## Configuração do Banco de Dados

### 1. Executar Scripts SQL

```sql
-- 1. Execute o script principal
@mentalance.sql

-- 2. Execute as sequences
@src/main/resources/db/oracle-sequences.sql
```

### 2. Configurar application.properties

Ajuste as seguintes propriedades no arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:XE
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

## Procedures Oracle Disponíveis

O projeto está configurado para usar as seguintes procedures do Oracle:

### 1. `inserts.inserir_usuario`
Insere um novo usuário usando validações do Oracle.

**Parâmetros:**
- `p_ID_USUARIO` (NUMBER)
- `p_NOME` (VARCHAR2)
- `p_EMAIL` (VARCHAR2)
- `p_SENHA` (VARCHAR2)
- `p_CARGO` (VARCHAR2)

### 2. `inserts.inserir_checkin`
Insere um novo check-in usando validações do Oracle.

**Parâmetros:**
- `p_ID_CHECKIN` (NUMBER)
- `p_DATA_CHECKIN` (TIMESTAMP)
- `p_EMOCAO` (VARCHAR2)
- `p_TEXTO` (VARCHAR2)
- `p_analise_sent` (VARCHAR2)
- `p_resposta` (VARCHAR2)
- `p_ID_USUARIO` (NUMBER)

### 3. `inserts.inserir_analise_semanal`
Insere uma análise semanal usando validações do Oracle.

**Parâmetros:**
- `p_ID_ANALISE` (NUMBER)
- `p_semana_ref` (VARCHAR2) - formato: YYYY-WNN
- `p_EMOCAO_pred` (VARCHAR2)
- `p_RESUMO` (VARCHAR2)
- `p_RECOMENDACAO` (VARCHAR2)
- `p_ID_USUARIO` (NUMBER)

### 4. `pkg_exportacao_json.pr_exportar_dataset_json`
Exporta todo o dataset em formato JSON.

**Retorna:** CLOB com JSON completo

### 5. `pkg_funcoes_json.fn_dataset_completo_json`
Função que retorna o dataset completo em JSON.

**Retorna:** CLOB com JSON completo

## Uso das Procedures no Backend

### Usando Procedures para Inserção

No `UsuarioService` e `CheckinService`, você pode escolher usar procedures ou JPA padrão:

```java
// Usar procedure Oracle
usuarioService.registrarUsuario(request, true);

// Usar JPA padrão
usuarioService.registrarUsuario(request, false);
```

### Endpoints REST para Exportação JSON

- `GET /api/oracle/exportar-json` - Usa a procedure `pr_exportar_dataset_json`
- `GET /api/oracle/dataset-json` - Usa a função `fn_dataset_completo_json`

## Estrutura das Tabelas

### USUARIO
- ID_USUARIO (NUMBER) - Primary Key
- NOME (VARCHAR2(100))
- EMAIL (VARCHAR2(100)) - Unique
- SENHA (VARCHAR2(2000))
- CARGO (VARCHAR2(100))
- DATA_CADASTRO (TIMESTAMP)

### CHECKIN
- ID_CHECKIN (NUMBER) - Primary Key
- DATA_CHECKIN (TIMESTAMP)
- EMOCAO (VARCHAR2(100))
- TEXTO (VARCHAR2(100))
- ANALISE_SENTIMENTO (VARCHAR2(100))
- RESPOSTA_GERADA (VARCHAR2(2000))
- USUARIO_ID_USUARIO (NUMBER) - Foreign Key

### ANALISE_SEMANAL
- ID_ANALISE (NUMBER) - Primary Key
- SEMANA_REFERENCIA (VARCHAR2(100)) - formato: YYYY-WNN
- EMOCAO_PREDOMINANTE (VARCHAR2(100))
- RESUMO (VARCHAR2(2000))
- RECOMENDACAO (VARCHAR2(2000))
- USUARIO_ID_USUARIO (NUMBER) - Foreign Key

### AUDITORIA
- id_audit (INTEGER) - Primary Key
- ID_USUARIO (INTEGER)
- NOME_usuario (VARCHAR2(100))
- operacao (VARCHAR2(10))
- data_hora (DATE)
- valor_antigo (VARCHAR2(2000))
- valor_novo (VARCHAR2(2000))

## Notas Importantes

1. **Sequences**: As sequences são necessárias para geração automática de IDs. 
   - **Se você NÃO tem dados nas tabelas**: Execute `oracle-sequences-simple.sql`
   - **Se você JÁ tem dados nas tabelas**: Execute `oracle-sequences.sql` (detecta automaticamente o maior ID e ajusta)
   - **Se a sequence já existe mas está com valor errado**: Execute `ajustar-sequences-existente.sql`

2. **Validações**: As procedures do Oracle incluem validações (email válido, senha mínima, etc.). Erros são lançados como exceções Oracle.

3. **Auditoria**: Todas as operações de INSERT, UPDATE e DELETE nas tabelas principais são automaticamente auditadas através de triggers.

4. **Autenticação**: O sistema agora usa EMAIL em vez de USERNAME para autenticação.

5. **IDs**: Os IDs podem ser gerados de duas formas:
   - Usando sequences (recomendado): `USUARIO_SEQ.NEXTVAL`
   - Usando MAX(id) + 1 (fallback): através dos métodos `getNextId()` nos repositories

