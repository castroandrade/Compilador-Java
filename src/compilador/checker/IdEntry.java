package compilador.checker;

import compilador.lexer.Token;
import compilador.lexer.TokenType;

/**
 * Representa uma entrada na Tabela de Símbolos.
 * Armazena informações sobre um identificador, como seu tipo.
 */
public class IdEntry {
    public final Token token;
    public TokenType type;

    public IdEntry(Token token, TokenType type) {
        this.token = token;
        this.type = type;
    }
}