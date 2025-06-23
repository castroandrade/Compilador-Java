package compilador.visitor;

import compilador.ast.*;
import compilador.checker.IdEntry;
import compilador.checker.SymbolTable;
import compilador.lexer.TokenType;

/**
 * Implementação do padrão Visitor para realizar a Análise de Contexto (escopo e tipos).
 * Este visitor percorre a AST construída pelo Parser e verifica as seguintes regras:
 * 1. Regras de Escopo: Garante que variáveis não sejam declaradas mais de uma vez e que sejam declaradas antes do uso.
 * 2. Regras de Tipos: Garante que as operações, atribuições e condições usem tipos compatíveis.
 *
 * O método `accept` para este visitor retorna o `TokenType` de uma expressão, ou `null` para comandos e declarações.
 */
public class CheckerVisitor implements Visitor<TokenType> {

    private final SymbolTable symbolTable;

    public CheckerVisitor() {
        this.symbolTable = new SymbolTable();
    }

    /**
     * Permite que outras classes acessem a tabela de símbolos
     * já preenchida e validada.
     */
    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    // --- MÉTODOS DE VISITA ---

    @Override
    public TokenType visitProgramaNode(ProgramaNode node) {
        // 1. Visita todas as declarações para popular a tabela de símbolos
        for (ASTNode decl : node.declaracoes) {
            decl.accept(this);
        }
        // 2. Visita o corpo principal do programa para checar os comandos
        node.comandoComposto.accept(this);
        return null;
    }

    @Override
    public TokenType visitVarDeclNode(VarDeclNode node) {
        IdEntry entry = new IdEntry(node.identifier, node.type.type);
        symbolTable.add(entry);
        return null;
    }

    @Override
    public TokenType visitBeginEndNode(BeginEndNode node) {
        // Visita cada comando dentro do bloco begin-end
        for (ASTNode cmd : node.commands) {
            cmd.accept(this);
        }
        return null;
    }

    @Override
    public TokenType visitAssignNode(AssignNode node) {
        // REGRA DE IDENTIFICAÇÃO: Verifica se a variável à esquerda foi declarada
        IdEntry entry = symbolTable.find(node.variable.lexeme);
        if (entry == null) {
            throw new Error("Erro de Contexto: Variavel '" + node.variable.lexeme + "' nao declarada (linha " + node.variable.line + ").");
        }

        // REGRA DE TIPOS: Verifica o tipo da expressão à direita
        TokenType exprType = node.expression.accept(this);

        // REGRA DE TIPOS: Verifica se os tipos são compatíveis para atribuição
        if (entry.type != exprType) {
            throw new Error("Erro de Contexto: Tipos incompativeis para atribuicao na linha " + node.variable.line + ". Esperado " +
                    entry.type + " mas encontrou " + exprType);
        }
        return null;
    }

    @Override
    public TokenType visitIfNode(IfNode node) {
        // REGRA DE TIPOS: A condição de um IF deve ser sempre booleana
        if (node.condition.accept(this) != TokenType.BOOLEAN) {
            throw new Error("Erro de Contexto: A expressao de condicao do IF deve ser do tipo BOOLEAN.");
        }
        // Visita as ramificações
        node.thenBranch.accept(this);
        if (node.elseBranch != null) {
            node.elseBranch.accept(this);
        }
        return null;
    }

    @Override
    public TokenType visitWhileNode(WhileNode node) {
        // REGRA DE TIPOS: A condição de um WHILE deve ser sempre booleana
        if (node.condition.accept(this) != TokenType.BOOLEAN) {
            throw new Error("Erro de Contexto: A expressao de condicao do WHILE deve ser do tipo BOOLEAN.");
        }
        // Visita o corpo do laço
        node.body.accept(this);
        return null;
    }

    @Override
    public TokenType visitBinaryOpNode(BinaryOpNode node) {
        TokenType leftType = node.left.accept(this);
        TokenType rightType = node.right.accept(this);

        switch (node.operator.type) {
            case PLUS:
            case MINUS:
            case TIMES:
            case DIV:
                // REGRA DE TIPOS: Operadores aritméticos exigem operandos do tipo INTEGER
                if (leftType != TokenType.INTEGER || rightType != TokenType.INTEGER) {
                    throw new Error("Erro de Contexto: Operador '" + node.operator.lexeme + "' exige operandos do tipo INTEGER (linha " + node.operator.line + ").");
                }
                return TokenType.INTEGER; // O resultado de uma operação aritmética é INTEGER

            case AND:
            case OR:
                // REGRA DE TIPOS: Operadores lógicos exigem operandos do tipo BOOLEAN
                if (leftType != TokenType.BOOLEAN || rightType != TokenType.BOOLEAN) {
                    throw new Error("Erro de Contexto: Operador '" + node.operator.lexeme + "' exige operandos do tipo BOOLEAN (linha " + node.operator.line + ").");
                }
                return TokenType.BOOLEAN; // O resultado de uma operação lógica é BOOLEAN

            case LT:
            case GT:
            case EQ:
                // REGRA DE TIPOS: Operadores relacionais exigem operandos do mesmo tipo
                if (leftType != rightType) {
                    throw new Error("Erro de Contexto: Operador '" + node.operator.lexeme + "' exige operandos de tipos compativeis (linha " + node.operator.line + ").");
                }
                return TokenType.BOOLEAN; // O resultado de uma operação relacional é sempre BOOLEAN
        }
        return null;
    }

    @Override
    public TokenType visitIntLitNode(IntLitNode node) {
        return TokenType.INTEGER;
    }

    @Override
    public TokenType visitBooleanLitNode(BooleanLitNode node) {
        return TokenType.BOOLEAN;
    }

    @Override
    public TokenType visitVariableUseNode(VariableUseNode node) {
        // REGRA DE IDENTIFICAÇÃO: Verifica se a variável foi declarada antes de ser usada
        IdEntry entry = symbolTable.find(node.identifier.lexeme);
        if (entry == null) {
            throw new Error("Erro de Contexto: Variavel '" + node.identifier.lexeme + "' nao declarada (linha " + node.identifier.line + ").");
        }
        // Retorna o tipo da variável que foi encontrado na Tabela de Símbolos
        return entry.type;
    }
}