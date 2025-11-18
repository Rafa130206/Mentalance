# 🚀 Guia de Execução - Mentalance

## 📋 Pré-requisitos

1. **Java 17** instalado
2. **Maven** instalado (ou use o Maven Wrapper incluído)
3. **Oracle Database** configurado e acessível
4. **Insomnia** ou **Postman** instalado

## 🔧 Passo 1: Configurar o Banco de Dados Oracle

### 1.1 Executar Scripts SQL

Antes de rodar a aplicação, você precisa executar os scripts SQL no Oracle:

1. **Execute o script principal** `mentalance.sql`:
   ```sql
   -- No SQL Developer ou SQL*Plus, execute:
   @caminho/para/mentalance.sql
   ```

2. **Execute o script de sequences**:
   
   **Se você JÁ tem dados nas tabelas (ex: 10 registros):**
   ```sql
   -- Este script detecta automaticamente o maior ID e ajusta a sequence
   @src/main/resources/db/oracle-sequences.sql
   ```
   
   **Se você NÃO tem dados ainda:**
   ```sql
   -- Script simples que começa do 1
   @src/main/resources/db/oracle-sequences-simple.sql
   ```
   
   **Se a sequence já existe mas está com valor incorreto:**
   ```sql
   -- Ajusta sequences existentes sem recriá-las
   @src/main/resources/db/ajustar-sequences-existente.sql
   ```

### 1.2 Verificar Configuração do Banco

O arquivo `application.properties` já está configurado com suas credenciais:
```properties
spring.datasource.url=jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl
spring.datasource.username=rm556459
spring.datasource.password=130206
```

**✅ URL já está no formato correto**: `jdbc:oracle:thin:@HOST:PORT:SID`

## 🏃 Passo 2: Executar o Projeto

### Opção 1: Usando Maven Wrapper (Recomendado)

**Windows (PowerShell ou CMD):**
```bash
.\mvnw.cmd spring-boot:run
```

**Linux/Mac:**
```bash
./mvnw spring-boot:run
```

### Opção 2: Usando Maven Instalado

```bash
mvn spring-boot:run
```

### Opção 3: Usando IDE (IntelliJ, Eclipse, VS Code)

1. Importe o projeto como projeto Maven
2. Localize a classe `MentalanceApplication.java`
3. Execute como aplicação Java

### Verificar se está rodando

Após iniciar, você deve ver mensagens como:
```
Started MentalanceApplication in X.XXX seconds
```

A aplicação estará disponível em: **http://localhost:8080**

## 🌐 Passo 3: Endpoints Disponíveis

### Endpoints Web (Thymeleaf - Requer Autenticação)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/` ou `/dashboard` | Dashboard do usuário |
| GET | `/login` | Página de login |
| GET | `/register` | Página de registro |
| GET | `/checkins` | Lista de check-ins |
| GET | `/checkins/novo` | Formulário de novo check-in |
| GET | `/relatorio` | Relatórios |
| GET | `/admin` | Painel administrativo |

### Endpoints REST API (JSON)

| Método | Endpoint | Descrição | Autenticação |
|--------|----------|-----------|--------------|
| GET | `/api/oracle/exportar-json` | Exporta dataset via procedure | ❌ Não |
| GET | `/api/oracle/dataset-json` | Obtém dataset via função | ❌ Não |

## 📮 Passo 4: Testar Endpoints no Insomnia/Postman

### 4.1 Configuração Inicial

1. Abra o **Insomnia** ou **Postman**
2. Crie uma nova **Collection** chamada "Mentalance API"
3. Configure a **Base URL**: `http://localhost:8080`

### 4.2 Endpoint 1: Exportar Dataset JSON (Procedure)

**Insomnia:**
1. Clique em **New Request**
2. Nome: `Exportar Dataset JSON (Procedure)`
3. Método: **GET**
4. URL: `http://localhost:8080/api/oracle/exportar-json`
5. Clique em **Send**

**Postman:**
1. Clique em **New** → **HTTP Request**
2. Método: **GET**
3. URL: `http://localhost:8080/api/oracle/exportar-json`
4. Clique em **Send**

**Resposta Esperada:**
```json
{
  "usuarios": [
    {
      "id_usuario": 1,
      "nome": "Administrador",
      "email": "admin@mentalance.com",
      "cargo": "Administrador",
      "checkins": [...],
      "analises_semanal": [...]
    }
  ]
}
```

### 4.3 Endpoint 2: Obter Dataset JSON (Função)

**Insomnia:**
1. Clique em **New Request**
2. Nome: `Obter Dataset JSON (Função)`
3. Método: **GET**
4. URL: `http://localhost:8080/api/oracle/dataset-json`
5. Clique em **Send**

**Postman:**
1. Clique em **New** → **HTTP Request**
2. Método: **GET**
3. URL: `http://localhost:8080/api/oracle/dataset-json`
4. Clique em **Send**

**Resposta Esperada:**
```json
{
  "usuarios": [
    {
      "id_usuario": 1,
      "nome": "Administrador",
      "email": "admin@mentalance.com",
      "cargo": "Administrador",
      "checkins": [...],
      "analises_semanal": [...]
    }
  ]
}
```

## 🔐 Passo 5: Acessar Interface Web

### 5.1 Login

1. Abra o navegador em: `http://localhost:8080`
2. Você será redirecionado para `/login`
3. Use as credenciais:
   - **Email**: `admin@mentalance.com`
   - **Senha**: `admin123`

### 5.2 Criar Novo Usuário

1. Acesse: `http://localhost:8080/register`
2. Preencha o formulário:
   - Nome
   - Email
   - Senha
   - Confirmar Senha
   - Cargo (opcional)
3. Clique em **Registrar**

### 5.3 Criar Check-in

1. Após login, vá para: `http://localhost:8080/checkins/novo`
2. Preencha:
   - **Emoção**: Ex: "Feliz", "Triste", "Ansioso"
   - **Texto**: Descrição do check-in (máx. 100 caracteres)
3. Clique em **Enviar**

## 🐛 Troubleshooting

### Erro: "Cannot connect to database"

**Solução:**
1. Verifique se o Oracle está rodando
2. Confirme as credenciais em `application.properties`
3. Verifique a URL de conexão (formato correto)
4. Teste a conexão manualmente:
   ```bash
   sqlplus rm556459/130206@oracle.fiap.com.br:1521/orcl
   ```

### Erro: "Table or view does not exist"

**Solução:**
1. Execute o script `mentalance.sql` no banco
2. Execute o script `oracle-sequences.sql`
3. Verifique se as tabelas foram criadas:
   ```sql
   SELECT table_name FROM user_tables;
   ```

### Erro: "Sequence does not exist"

**Solução:**
1. Execute o script `src/main/resources/db/oracle-sequences.sql`
2. Verifique se as sequences foram criadas:
   ```sql
   SELECT sequence_name FROM user_sequences;
   ```

### Erro: "Procedure or function does not exist"

**Solução:**
1. Verifique se executou o script `mentalance.sql` completo
2. Confirme que as procedures foram criadas:
   ```sql
   SELECT object_name, object_type 
   FROM user_objects 
   WHERE object_type IN ('PROCEDURE', 'FUNCTION', 'PACKAGE');
   ```

### Aplicação não inicia

**Solução:**
1. Verifique se a porta 8080 está livre:
   ```bash
   # Windows
   netstat -ano | findstr :8080
   
   # Linux/Mac
   lsof -i :8080
   ```
2. Altere a porta em `application.properties`:
   ```properties
   server.port=8081
   ```

## 📊 Exemplos de Respostas JSON

### Resposta de Sucesso (Exportar JSON)
```json
{
  "usuarios": [
    {
      "id_usuario": 1,
      "nome": "Administrador",
      "email": "admin@mentalance.com",
      "cargo": "Administrador",
      "checkins": [
        {
          "id_checkin": 1,
          "emocao": "Feliz",
          "texto": "Dia produtivo",
          "analise_sentimento": "positivo",
          "resposta_gerada": "Continue assim! Mantenha o foco e a energia positiva."
        }
      ],
      "analises_semanal": [
        {
          "semana": "2025-W01",
          "emocao_predominante": "Feliz",
          "resumo": "Semana produtiva",
          "recomendacao": "Continue assim"
        }
      ]
    }
  ]
}
```

### Resposta de Erro
```json
{
  "erro": "ORA-XXXXX: mensagem de erro do Oracle"
}
```

## 📝 Notas Importantes

1. **Primeira Execução**: O sistema criará automaticamente o usuário admin se não existir nenhum usuário no banco.

2. **Procedures Oracle**: Os endpoints `/api/oracle/*` chamam diretamente as procedures/funções do Oracle definidas no script SQL.

3. **Autenticação Web**: A interface web usa Spring Security. Para acessar endpoints protegidos, você precisa fazer login primeiro.

4. **Logs**: Os logs SQL estão habilitados. Você pode ver as queries executadas no console.

5. **Porta**: A aplicação roda na porta 8080 por padrão. Se estiver em uso, altere em `application.properties`.

## 🎯 Checklist de Execução

- [ ] Oracle Database configurado e acessível
- [ ] Script `mentalance.sql` executado
- [ ] Script `oracle-sequences.sql` executado
- [ ] Credenciais do banco configuradas em `application.properties`
- [ ] Aplicação iniciada sem erros
- [ ] Endpoints REST testados no Insomnia/Postman
- [ ] Login web funcionando
- [ ] Check-in criado com sucesso

## 📞 Suporte

Se encontrar problemas:
1. Verifique os logs da aplicação no console
2. Confirme que todas as tabelas e procedures foram criadas
3. Teste a conexão com o banco manualmente
4. Verifique se a porta 8080 está disponível

