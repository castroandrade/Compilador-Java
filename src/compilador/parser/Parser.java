package compilador.parser;

import compilador.ast.*;
import compilador.lexer.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Parser (Analisador Sintático) que implementa o método descendente recursivo.
 * Ele consome uma lista de tokens do Scanner e constrói uma Árvore Sintática Abstrata (AST).
 */
public class Parser {
    private final List<Token> tokens;
    private int current = 0;
    private Token currentToken;

    public Parser(Scanner scanner) {
        this.tokens = scanner.scanTokens();
        this.currentToken = tokens.get(current);
    }

    /**
     * Avança para o próximo token na lista.
     * @return O novo token atual.
     */
    private Token advance() {
        if (current < tokens.size() - 1) {
            current++;
        }
        currentToken = tokens.get(current);
        return currentToken;
    }

    /**
     * Método público para iniciar a análise sintática.
     * @return A raiz da AST construída.
     */
    public ASTNode parse() {
        return program();
    }

    /**
     * Verifica se o token atual corresponde ao tipo esperado e avança.
     * @param expected O tipo de token esperado.
     * @throws Error se o token atual não corresponder ao esperado.
     */
    private void match(TokenType expected) {
        if (currentToken.type == expected) {
            advance();
        } else {
            throw new Error("Syntax Error: expected " + expected +
                    " but found " + currentToken.type + " at line " + currentToken.line);
        }
    }

    // --- Métodos de Parsing (um para cada regra da gramática LL(1)) ---

    private ProgramaNode program() {
        match(TokenType.PROGRAM);
        match(TokenType.ID); // O nome do programa não é armazenado na AST por enquanto
        match(TokenType.SEMICOLON);

        List<ASTNode> decls = declarations();
        ASTNode compStmt = compoundStatement();

        // A gramática pode ter um ponto final aqui
        // match(TokenType.DOT);

        return new ProgramaNode(decls, compStmt);
    }

    private List<ASTNode> declarations() {
        List<ASTNode> decls = new ArrayList<>();
        while (currentToken.type == TokenType.VAR) {
            decls.add(variableDeclaration());
        }
        return decls;
    }

    private VarDeclNode variableDeclaration() {
        match(TokenType.VAR);
        Token id = currentToken;
        match(TokenType.ID);
        match(TokenType.COLON);
        Token type = currentToken;
        // Assume que o tipo pode ser qualquer token (ex: INTEGER, BOOLEAN) e avança
        advance();
        match(TokenType.SEMICOLON);
        return new VarDeclNode(id, type);
    }

    private ASTNode compoundStatement() {
        match(TokenType.BEGIN);
        List<ASTNode> stmts = statementList();
        match(TokenType.END);
        return new BeginEndNode(stmts);
    }

    private List<ASTNode> statementList() {
        List<ASTNode> stmts = new ArrayList<>();
        while (currentToken.type != TokenType.END && currentToken.type != TokenType.EOF) {
            stmts.add(statement());
            // Após cada comando na lista, exige um ponto-e-vírgula como separador
            match(TokenType.SEMICOLON);
        }
        return stmts;
    }

    private ASTNode statement() {
        switch (currentToken.type) {
            case ID:
                return assignment();
            case IF:
                return conditional();
            case WHILE:
                return iterative();
            case BEGIN:
                return compoundStatement();
            default:
                throw new RuntimeException("Unexpected statement starting with: " + currentToken.lexeme);
        }
    }

    private AssignNode assignment() {
        Token variable = currentToken;
        match(TokenType.ID);
        match(TokenType.ASSIGN);
        ASTNode expr = expression();
        return new AssignNode(variable, expr);
    }

    private IfNode conditional() {
        match(TokenType.IF);
        ASTNode condition = expression();
        match(TokenType.THEN);
        ASTNode thenBranch = statement();
        ASTNode elseBranch = null;
        if (currentToken.type == TokenType.ELSE) {
            match(TokenType.ELSE);
            elseBranch = statement();
        }
        return new IfNode(condition, thenBranch, elseBranch);
    }

    private WhileNode iterative() {
        match(TokenType.WHILE);
        ASTNode condition = expression();
        match(TokenType.DO);
        ASTNode body = statement();
        return new WhileNode(condition, body);
    }

    // --- Métodos de Parsing para Expressões ---

    private ASTNode expression() {
        ASTNode left = simpleExpression();
        while (currentToken.type == TokenType.LT || currentToken.type == TokenType.GT || currentToken.type == TokenType.EQ) {
            Token op = currentToken;
            match(op.type);
            ASTNode right = simpleExpression();
            left = new BinaryOpNode(left, op, right);
        }
        return left;
    }

    private ASTNode simpleExpression() {
        ASTNode left = term();
        while (currentToken.type == TokenType.PLUS || currentToken.type == TokenType.OR || currentToken.type == TokenType.MINUS) {
            Token op = currentToken;
            match(op.type);
            ASTNode right = term();
            left = new BinaryOpNode(left, op, right);
        }
        return left;
    }

    private ASTNode term() {
        ASTNode left = factor();
        while (currentToken.type == TokenType.TIMES || currentToken.type == TokenType.DIV || currentToken.type == TokenType.AND) {
            Token op = currentToken;
            match(op.type);
            ASTNode right = factor();
            left = new BinaryOpNode(left, op, right);
        }
        return left;
    }

    private ASTNode factor() {
        Token token = currentToken;
        if (token.type == TokenType.INT_LIT) {
            match(TokenType.INT_LIT);
            return new IntLitNode(token);
        } else if (token.type == TokenType.TRUE) {
            match(TokenType.TRUE);
            return new BooleanLitNode(token);
        } else if (token.type == TokenType.FALSE) {
            match(TokenType.FALSE);
            return new BooleanLitNode(token);
        } else if (token.type == TokenType.ID) {
            match(TokenType.ID);
            return new VariableUseNode(token);
        } else if (token.type == TokenType.LPAREN) {
            match(TokenType.LPAREN);
            ASTNode node = expression();
            match(TokenType.RPAREN);
            return node;
        }
        throw new RuntimeException("Unexpected factor: " + token.lexeme);
    }
}