package compilador.lexer;

/**
 * Representa um token, a unidade fundamental da análise léxica.
 * Um token possui um tipo, o lexema (o texto original do código-fonte),
 * um valor literal (para números, por exemplo) e a linha onde foi encontrado.
 */
public class Token {

    /**
     * O tipo do token, definido pela enumeração TokenType.
     */
    public final TokenType type;

    /**
     * O trecho de texto do código-fonte que gerou este token.
     * Ex: "var", "soma", "123", ":="
     */
    public final String lexeme;

    /**
     * O valor real de um token literal.
     * Para um INT_LIT "123", o literal é o Integer 123.
     * Para um FLOAT_LIT "10.5", o literal é o Double 10.5.
     * Para outros tokens, é null.
     */
    public final Object literal;

    /**
     * A linha no código-fonte onde o lexema se inicia.
     * Usado para reportar erros de forma clara.
     */
    public final int line;

    /**
     * Construtor para a classe Token.
     *
     * @param type    O tipo do token (da enumeração TokenType).
     * @param lexeme  A string exata do código-fonte.
     * @param literal O valor literal (pode ser null).
     * @param line    A linha onde o token foi encontrado.
     */
    public Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    /**
     * Gera uma representação textual do token, útil para depuração.
     *
     * @return Uma string formatada descrevendo o token.
     */
    @Override
    public String toString() {
        // Exemplo de saída: "Token [type=ID, lexeme='soma', literal=null, line=5]"
        // Ou: "Token [type=INT_LIT, lexeme='123', literal=123, line=6]"
        return "Token [type=" + type + ", lexeme='" + lexeme + "', literal=" + literal + ", line=" + line + "]";
    }
}