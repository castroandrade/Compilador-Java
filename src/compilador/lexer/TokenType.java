package compilador.lexer;

/**
 * Enumeração que representa todos os tipos de tokens possíveis na linguagem.
 * Cada variante corresponde a uma unidade lexical, como uma palavra-chave,
 * um operador, um literal ou um identificador.
 */

public enum TokenType {
    // --- Pontuação e Operadores de um caractere ---
    LPAREN,     // (
    RPAREN,     // )
    SEMICOLON,  // ;
    PLUS,       // +
    MINUS,      // -
    TIMES,      // *
    DIV,        // /
    LT,         // <
    GT,         // >
    EQ,         // =
    // --- Operadores de um ou mais caracteres ---
    COLON, // :
    ASSIGN,     // :=

    // --- Literais ---
    ID,         // Identificadores: soma, val1, etc.
    INT_LIT,    // Literal Inteiro: 123, 42
    FLOAT_LIT,  // Literal Float: 10.5, 0.5

    // --- Palavras-Chave ---
    PROGRAM,
    VAR,
    BEGIN,
    END,
    IF,
    THEN,
    ELSE,
    WHILE,
    DO,
    INTEGER,
    BOOLEAN,
    TRUE,
    FALSE,
    OR,
    AND,

    EOF // Fim de Arquivo (End Of File)
}