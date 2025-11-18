# Mentalance

Plataforma web de bem-estar emocional construída com Spring Boot e Thymeleaf. Permite que usuários realizem check-ins diários, acompanhem relatórios com gráficos e recebam insights gerados por um serviço de IA (mockado, pronto para integração futura). Inclui painel administrativo com estatísticas gerais.

## Funcionalidades

- Registro e autenticação de usuários (Spring Security + BCrypt).
- Check-ins diários com dados de humor, energia, sono e contexto textual.
- Geração de análises com base em regras simulando uma IA generativa.
- Painel do usuário com gráficos (Chart.js) e histórico de análises.
- Painel administrativo com visão geral do sistema.
- Internacionalização (`pt-BR` e `en-US`).
- Validação de formulários e tratamento centralizado de erros.

## Stack

- Java 17
- Spring Boot 3.5.4 (Web, Data JPA, Security, Validation, Thymeleaf)
- H2 (Ambiente de desenvolvimento) / Oracle (produção)
- Thymeleaf + Chart.js

## Executando o projeto

### Pré-requisitos

1. Java 17 instalado
2. Oracle Database configurado
3. Scripts SQL executados (veja `GUIA_EXECUCAO.md`)

### Executar

**Windows:**
```bash
.\mvnw.cmd spring-boot:run
```

**Linux/Mac:**
```bash
./mvnw spring-boot:run
```

Aplicação disponível em `http://localhost:8080`.

### Credenciais iniciais

- **Email**: `admin@mentalance.com`
- **Senha**: `admin123`

> ⚠️ **IMPORTANTE**: O sistema agora usa **EMAIL** em vez de username para login.

## Configuração do banco

O projeto está configurado para usar **Oracle Database**. 

### Passos para configurar:

1. Execute o script `mentalance.sql` no Oracle
2. Execute o script `src/main/resources/db/oracle-sequences.sql`
3. As credenciais já estão configuradas em `application.properties`

### Endpoints REST API

- `GET /api/oracle/exportar-json` - Exporta dataset via procedure Oracle
- `GET /api/oracle/dataset-json` - Obtém dataset via função Oracle

📖 **Para instruções detalhadas de execução e testes, consulte o [GUIA_EXECUCAO.md](GUIA_EXECUCAO.md)**

## Próximos passos sugeridos

- Integrar serviço real de IA (Azure OpenAI, OpenAI, etc.) implementando `IAFeedbackService`.
- Persistir análises históricas em serviços externos ou data lake.
- Automatizar deploy (Azure Web Apps ou Render).
- Adicionar testes automatizados para serviços e controladores.

## Autores

- Nome do Autor 1 — RM XXXXX
- Nome do Autor 2 — RM XXXXX
- Nome do Autor 3 — RM XXXXX

Atualize a seção com os participantes do time.

