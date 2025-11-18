package br.com.fiap.mentalance.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * Estratégia de nomenclatura customizada para Oracle
 * Garante que os nomes de tabelas, colunas e sequences sejam preservados
 * exatamente como definidos nas anotações (maiúsculas)
 */
public class OracleNamingStrategy implements PhysicalNamingStrategy {

    @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return apply(name);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return apply(name);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        // Preserva o nome exatamente como está (maiúsculas)
        return apply(name);
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        // Preserva o nome da sequence exatamente como está (maiúsculas)
        // Importante: não usar aspas para que o Oracle converta automaticamente
        if (name != null) {
            String text = name.getText().toUpperCase();
            return Identifier.toIdentifier(text, false);
        }
        return name;
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        // Preserva o nome da coluna exatamente como está (maiúsculas)
        return apply(name);
    }

    private Identifier apply(Identifier name) {
        if (name == null) {
            return null;
        }
        // Se o nome já está em maiúsculas, preserva; senão converte
        String text = name.getText();
        // Se já está em maiúsculas (como definido nas anotações), preserva
        if (text.equals(text.toUpperCase())) {
            return Identifier.toIdentifier(text, false);
        }
        // Caso contrário, converte para maiúsculas
        return Identifier.toIdentifier(text.toUpperCase(), false);
    }
}

