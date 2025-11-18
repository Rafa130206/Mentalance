-- Sequences para geração automática de IDs
-- Execute este script no Oracle após executar o script principal mentalance.sql
-- 
-- IMPORTANTE: Este script detecta automaticamente o maior ID existente
-- e configura a sequence para começar do próximo valor disponível

-- ============================================
-- Sequence para USUARIO
-- ============================================
-- Remove sequence se já existir
BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE USUARIO_SEQ';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -2289 THEN -- Sequence não existe
            RAISE;
        END IF;
END;
/

-- Cria sequence começando do próximo ID disponível
DECLARE
    v_max_id NUMBER := 0;
    v_start_with NUMBER;
BEGIN
    -- Obtém o maior ID existente (ou 0 se não houver dados)
    SELECT NVL(MAX(ID_USUARIO), 0) INTO v_max_id FROM USUARIO;
    
    -- Define o próximo valor (máximo + 1)
    v_start_with := v_max_id + 1;
    
    -- Se não há dados, começa em 1
    IF v_start_with < 1 THEN
        v_start_with := 1;
    END IF;
    
    -- Cria a sequence
    EXECUTE IMMEDIATE 'CREATE SEQUENCE USUARIO_SEQ START WITH ' || v_start_with || ' INCREMENT BY 1';
    
    DBMS_OUTPUT.PUT_LINE('USUARIO_SEQ criada começando em: ' || v_start_with);
END;
/

-- ============================================
-- Sequence para CHECKIN
-- ============================================
-- Remove sequence se já existir
BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE CHECKIN_SEQ';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -2289 THEN
            RAISE;
        END IF;
END;
/

-- Cria sequence começando do próximo ID disponível
DECLARE
    v_max_id NUMBER := 0;
    v_start_with NUMBER;
BEGIN
    SELECT NVL(MAX(ID_CHECKIN), 0) INTO v_max_id FROM CHECKIN;
    v_start_with := v_max_id + 1;
    
    IF v_start_with < 1 THEN
        v_start_with := 1;
    END IF;
    
    EXECUTE IMMEDIATE 'CREATE SEQUENCE CHECKIN_SEQ START WITH ' || v_start_with || ' INCREMENT BY 1';
    
    DBMS_OUTPUT.PUT_LINE('CHECKIN_SEQ criada começando em: ' || v_start_with);
END;
/

-- ============================================
-- Sequence para ANALISE_SEMANAL
-- ============================================
-- Remove sequence se já existir
BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE ANALISE_SEMANAL_SEQ';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -2289 THEN
            RAISE;
        END IF;
END;
/

-- Cria sequence começando do próximo ID disponível
DECLARE
    v_max_id NUMBER := 0;
    v_start_with NUMBER;
BEGIN
    SELECT NVL(MAX(ID_ANALISE), 0) INTO v_max_id FROM ANALISE_SEMANAL;
    v_start_with := v_max_id + 1;
    
    IF v_start_with < 1 THEN
        v_start_with := 1;
    END IF;
    
    EXECUTE IMMEDIATE 'CREATE SEQUENCE ANALISE_SEMANAL_SEQ START WITH ' || v_start_with || ' INCREMENT BY 1';
    
    DBMS_OUTPUT.PUT_LINE('ANALISE_SEMANAL_SEQ criada começando em: ' || v_start_with);
END;
/

-- ============================================
-- Verificação final
-- ============================================
SET SERVEROUTPUT ON
SELECT 'Sequences criadas com sucesso!' AS STATUS FROM DUAL;
SELECT sequence_name, last_number FROM user_sequences 
WHERE sequence_name IN ('USUARIO_SEQ', 'CHECKIN_SEQ', 'ANALISE_SEMANAL_SEQ')
ORDER BY sequence_name;

