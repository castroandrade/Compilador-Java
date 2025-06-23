package compilador.lexer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * A classe Scanner, também conhecida como Analisador Léxico ou Lexer.
 * Sua responsabilidade é converter uma string de código-fonte em uma lista de Tokens.
 * Ela agrupa caracteres em lexemas e os associa a um tipo de token.
 */
public class Scanner {

    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    // Ponteiros para controlar a análise no código-fonte
    private int start = 0;   // Início do lexema atual
    private int current = 0; // Caractere sendo analisado
    private int line = 1;    // Linha atual, para reportar erros

    // Mapa estático para armazenar e consultar palavras-chave da linguagem
    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("program", TokenType.PROGRAM);
        keywords.put("var",     TokenType.VAR);
        keywords.put("begin",   TokenType.BEGIN);
        keywords.put("end",     TokenType.END);
        keywords.put("if",      TokenType.IF);
        keywords.put("then",    TokenType.THEN);
        keywords.put("else",    TokenType.ELSE);
        keywords.put("while",   TokenType.WHILE);
        keywords.put("do",      TokenType.DO);
        keywords.put("integer", TokenType.INTEGER);
        keywords.put("boolean", TokenType.BOOLEAN);
        keywords.put("true",    TokenType.TRUE);
        keywords.put("false",   TokenType.FALSE);
        keywords.put("or",      TokenType.OR);
        keywords.put("and",     TokenType.AND);
    }

    /**
     * Construtor do Scanner.
     * @param source O código-fonte completo como uma única String.
     */
    public Scanner(String source) {
        this.source = source;
    }

    /**
     * Método principal que executa a análise léxica.
     * @return Uma lista de tokens extraídos do código-fonte.
     */
    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            // Inicia um novo lexema no começo de cada iteração
            start = current;
            scanToken();
        }

        // Adiciona um token final para marcar o fim do arquivo (EOF)
        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    /**
     * Analisa e classifica o próximo token do código-fonte.
     */
    private void scanToken() {
        char c = advance();
        switch (c) {
            // Tokens de um único caractere
            case '(': addToken(TokenType.LPAREN); break;
            case ')': addToken(TokenType.RPAREN); break;
            case ';': addToken(TokenType.SEMICOLON); break;
            case '+': addToken(TokenType.PLUS); break;
            case '*': addToken(TokenType.TIMES); break;
            case '=': addToken(TokenType.EQ); break;
            case '-': addToken(TokenType.MINUS); break;

            // Operadores que podem ter mais de um caractere
            case ':':
                if (match('=')) {
                    addToken(TokenType.ASSIGN);
                } else {
                    addToken(TokenType.COLON);
                }
                break;

            case '<':
                addToken(TokenType.LT);
                break;
            case '>':
                addToken(TokenType.GT);
                break;

            case '/':
                if (match('/')) {
                    // É um comentário. Consome todos os caracteres até o fim da linha.
                    while (peek() != '\n' && !isAtEnd()) {
                        advance();
                    }
                } else {
                    // Se não for 'match', é apenas um operador de divisão.
                    addToken(TokenType.DIV);
                }
                break;

            // Ignorar espaços em branco
            case ' ':
            case '\r':
            case '\t':
                break;

            case '\n':
                line++;
                break;

            default:
                if (isDigit(c)) {
                    // Se for um dígito, começa a analisar um número
                    number();
                } else if (isAlpha(c)) {
                    // Se for uma letra, começa a analisar um identificador ou palavra-chave
                    identifier();
                } else {
                    // Se não for nenhum dos anteriores, é um caractere inválido
                    error(line, "Caractere não reconhecido: " + c);
                }
                break;
        }
    }

    /**
     * Processa um lexema que é um identificador ou uma palavra-chave.
     */
    private void identifier() {
        while (isAlphaNumeric(peek())) {
            advance();
        }

        String text = source.substring(start, current);
        TokenType type = keywords.get(text); // Verifica se o texto é uma palavra-chave
        if (type == null) {
            type = TokenType.ID; // Se não for, é um identificador
        }
        addToken(type);
    }

    /**
     * Processa um lexema que é um número (inteiro ou de ponto flutuante).
     */
    private void number() {
        while (isDigit(peek())) {
            advance();
        }

        // Verifica se há uma parte fracionária
        if (peek() == '.' && isDigit(peekNext())) {
            // Consome o '.'
            advance();

            while (isDigit(peek())) {
                advance();
            }
            // Adiciona o token como FLOAT_LIT
            addToken(TokenType.FLOAT_LIT, Double.parseDouble(source.substring(start, current)));
        } else {
            // Adiciona o token como INT_LIT
            addToken(TokenType.INT_LIT, Integer.parseInt(source.substring(start, current)));
        }
    }

    // --- Métodos Auxiliares ---

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private char advance() {
        return source.charAt(current++);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private void error(int line, String message) {
        // Método simples para reportar erros
        System.err.println("[Linha " + line + "] Erro Léxico: " + message);
    }
}