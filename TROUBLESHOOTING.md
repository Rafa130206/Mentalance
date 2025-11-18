# 🔧 Troubleshooting - Problemas Comuns

## Problema: Página de cadastro fica carregando infinitamente

### Sintomas
- Ao clicar em "Criar conta", a página fica carregando
- Logs mostram: `select usuario_seq.nextval from dual`
- Erro pode aparecer como timeout ou conexão perdida

### Causas Possíveis

#### 1. Sequence não existe no banco ou está com valor incorreto
**Verificar:**
```sql
-- Verificar se a sequence existe e qual é o próximo valor
SELECT sequence_name, last_number 
FROM user_sequences 
WHERE sequence_name = 'USUARIO_SEQ';

-- Verificar qual é o maior ID na tabela
SELECT MAX(ID_USUARIO) FROM USUARIO;
```

**Solução:**

**Se você JÁ tem dados (ex: 10 registros):**
```sql
-- Script que detecta automaticamente o maior ID e ajusta
@src/main/resources/db/oracle-sequences.sql
```

**Se a sequence já existe mas está com valor errado:**
```sql
-- Ajusta a sequence existente sem recriá-la
@src/main/resources/db/ajustar-sequences-existente.sql
```

**Se não tem dados ainda:**
```sql
-- Script simples que começa do 1
@src/main/resources/db/oracle-sequences-simple.sql
```

#### 2. Nome da sequence em minúsculas vs maiúsculas
**Problema:** Hibernate pode estar gerando `usuario_seq` mas a sequence é `USUARIO_SEQ`

**Solução:** Execute o script de sequences:
```sql
-- Execute este script no Oracle:
@src/main/resources/db/oracle-sequences.sql
```

#### 3. Tabela não existe ou nome incorreto
**Verificar:**
```sql
-- Verificar se a tabela existe
SELECT table_name FROM user_tables WHERE table_name = 'USUARIO';

-- Se não existir, execute o script principal:
@mentalance.sql
```

#### 4. Problema de permissões
**Verificar:**
```sql
-- Verificar permissões do usuário
SELECT * FROM user_tab_privs WHERE table_name = 'USUARIO';
```

### Soluções Aplicadas

1. ✅ Criada estratégia de nomenclatura customizada (`OracleNamingStrategy`)
2. ✅ Removida validação `@NotBlank` do campo `cargo` (agora é opcional)
3. ✅ Adicionado tratamento de erro melhor no controller
4. ✅ Garantido valor padrão para `cargo` quando vazio

### Como Testar

1. **Verificar sequences:**
```sql
SELECT sequence_name, last_number 
FROM user_sequences 
WHERE sequence_name IN ('USUARIO_SEQ', 'CHECKIN_SEQ', 'ANALISE_SEMANAL_SEQ');
```

2. **Testar sequence manualmente:**
```sql
SELECT USUARIO_SEQ.NEXTVAL FROM DUAL;
```

3. **Verificar tabelas:**
```sql
SELECT table_name FROM user_tables 
WHERE table_name IN ('USUARIO', 'CHECKIN', 'ANALISE_SEMANAL');
```

4. **Verificar estrutura da tabela:**
```sql
DESC USUARIO;
```

### Se o problema persistir

1. **Limpar cache do Hibernate:**
   - Reinicie a aplicação
   - Limpe o diretório `target/` se existir

2. **Verificar logs completos:**
   - Ative logs mais detalhados em `application.properties`:
   ```properties
   logging.level.org.hibernate=DEBUG
   logging.level.org.hibernate.SQL=DEBUG
   ```

3. **Testar conexão manualmente:**
```sql
-- Conecte no Oracle e teste:
INSERT INTO USUARIO (ID_USUARIO, NOME, EMAIL, SENHA, CARGO, DATA_CADASTRO)
VALUES (USUARIO_SEQ.NEXTVAL, 'Teste', 'teste@teste.com', 'senha123', 'Teste', SYSTIMESTAMP);
```

4. **Verificar se há locks:**
```sql
SELECT * FROM v$locked_object;
```

### Próximos Passos

Se ainda não funcionar:
1. Verifique os logs completos da aplicação
2. Confirme que todas as sequences foram criadas
3. Teste a inserção manual no Oracle
4. Verifique se há erros de constraint ou trigger

