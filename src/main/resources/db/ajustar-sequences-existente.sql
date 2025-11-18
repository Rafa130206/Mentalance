-- Script para AJUSTAR sequences existentes quando já há dados nas tabelas
-- Use este script se você já tem sequences criadas mas elas estão com valores incorretos

SET SERVEROUTPUT ON

-- ============================================
-- Ajustar USUARIO_SEQ
-- ============================================
DECLARE
    v_max_id NUMBER;
    v_current_val NUMBER;
    v_increment NUMBER;
BEGIN
    -- Obtém o maior ID existente
    SELECT NVL(MAX(ID_USUARIO), 0) INTO v_max_id FROM USUARIO;
    
    -- Obtém o valor atual da sequence
    SELECT USUARIO_SEQ.NEXTVAL INTO v_current_val FROM DUAL;
    
    -- Calcula quantos valores precisam ser incrementados
    v_increment := (v_max_id + 1) - v_current_val;
    
    IF v_increment > 0 THEN
        -- Incrementa a sequence até o valor correto
        EXECUTE IMMEDIATE 'ALTER SEQUENCE USUARIO_SEQ INCREMENT BY ' || v_increment;
        SELECT USUARIO_SEQ.NEXTVAL INTO v_current_val FROM DUAL;
        EXECUTE IMMEDIATE 'ALTER SEQUENCE USUARIO_SEQ INCREMENT BY 1';
        
        DBMS_OUTPUT.PUT_LINE('USUARIO_SEQ ajustada. Próximo valor: ' || (v_max_id + 1));
    ELSE
        DBMS_OUTPUT.PUT_LINE('USUARIO_SEQ já está correta. Próximo valor: ' || v_current_val);
    END IF;
END;
/

-- ============================================
-- Ajustar CHECKIN_SEQ
-- ============================================
DECLARE
    v_max_id NUMBER;
    v_current_val NUMBER;
    v_increment NUMBER;
BEGIN
    SELECT NVL(MAX(ID_CHECKIN), 0) INTO v_max_id FROM CHECKIN;
    SELECT CHECKIN_SEQ.NEXTVAL INTO v_current_val FROM DUAL;
    v_increment := (v_max_id + 1) - v_current_val;
    
    IF v_increment > 0 THEN
        EXECUTE IMMEDIATE 'ALTER SEQUENCE CHECKIN_SEQ INCREMENT BY ' || v_increment;
        SELECT CHECKIN_SEQ.NEXTVAL INTO v_current_val FROM DUAL;
        EXECUTE IMMEDIATE 'ALTER SEQUENCE CHECKIN_SEQ INCREMENT BY 1';
        
        DBMS_OUTPUT.PUT_LINE('CHECKIN_SEQ ajustada. Próximo valor: ' || (v_max_id + 1));
    ELSE
        DBMS_OUTPUT.PUT_LINE('CHECKIN_SEQ já está correta. Próximo valor: ' || v_current_val);
    END IF;
END;
/

-- ============================================
-- Ajustar ANALISE_SEMANAL_SEQ
-- ============================================
DECLARE
    v_max_id NUMBER;
    v_current_val NUMBER;
    v_increment NUMBER;
BEGIN
    SELECT NVL(MAX(ID_ANALISE), 0) INTO v_max_id FROM ANALISE_SEMANAL;
    SELECT ANALISE_SEMANAL_SEQ.NEXTVAL INTO v_current_val FROM DUAL;
    v_increment := (v_max_id + 1) - v_current_val;
    
    IF v_increment > 0 THEN
        EXECUTE IMMEDIATE 'ALTER SEQUENCE ANALISE_SEMANAL_SEQ INCREMENT BY ' || v_increment;
        SELECT ANALISE_SEMANAL_SEQ.NEXTVAL INTO v_current_val FROM DUAL;
        EXECUTE IMMEDIATE 'ALTER SEQUENCE ANALISE_SEMANAL_SEQ INCREMENT BY 1';
        
        DBMS_OUTPUT.PUT_LINE('ANALISE_SEMANAL_SEQ ajustada. Próximo valor: ' || (v_max_id + 1));
    ELSE
        DBMS_OUTPUT.PUT_LINE('ANALISE_SEMANAL_SEQ já está correta. Próximo valor: ' || v_current_val);
    END IF;
END;
/

-- Verificação final
SELECT 'Sequences ajustadas!' AS STATUS FROM DUAL;
SELECT sequence_name, last_number FROM user_sequences 
WHERE sequence_name IN ('USUARIO_SEQ', 'CHECKIN_SEQ', 'ANALISE_SEMANAL_SEQ')
ORDER BY sequence_name;

