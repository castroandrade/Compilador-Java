package compilador.visitor;

import compilador.ast.*;

/**
 * Implementação do padrão Visitor para criar uma representação textual
 * e indentada da Árvore Sintática Abstrata (AST).
 * Esta classe é usada para visualizar a estrutura do programa após a análise sintática,
 * cumprindo o requisito da Etapa 3 do projeto.
 */
public class ASTPrinter implements Visitor<String> {

    private int indentLevel = 0;

    /**
     * Gera uma string com espaços para a indentação atual.
     */
    private String indent() {
        return "  ".repeat(indentLevel);
    }

    @Override
    public String visitProgramaNode(ProgramaNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("ProgramaNode\n");

        indentLevel++;
        sb.append(indent()).append("Declaracoes:\n");
        indentLevel++;
        for (ASTNode decl : node.declaracoes) {
            sb.append(decl.accept(this));
        }
        indentLevel--;

        sb.append(indent()).append("Comando Composto:\n");
        indentLevel++;
        sb.append(node.comandoComposto.accept(this));
        indentLevel--;
        indentLevel--;

        return sb.toString();
    }

    @Override
    public String visitVarDeclNode(VarDeclNode node) {
        return indent() + "VarDeclNode: " + node.identifier.lexeme + " : " + node.type.lexeme + "\n";
    }

    @Override
    public String visitBeginEndNode(BeginEndNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("BeginEndNode\n");

        indentLevel++;
        for (ASTNode cmd : node.commands) {
            sb.append(cmd.accept(this));
        }
        indentLevel--;

        return sb.toString();
    }

    @Override
    public String visitAssignNode(AssignNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("AssignNode: ").append(node.variable.lexeme).append("\n");

        indentLevel++;
        sb.append(indent()).append("Expression:\n");
        sb.append(node.expression.accept(this));
        indentLevel--;

        return sb.toString();
    }

    @Override
    public String visitIfNode(IfNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("IfNode\n");

        indentLevel++;
        sb.append(indent()).append("Condition:\n");
        sb.append(node.condition.accept(this));

        sb.append(indent()).append("Then Branch:\n");
        sb.append(node.thenBranch.accept(this));

        if (node.elseBranch != null) {
            sb.append(indent()).append("Else Branch:\n");
            sb.append(node.elseBranch.accept(this));
        }
        indentLevel--;

        return sb.toString();
    }

    @Override
    public String visitWhileNode(WhileNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("WhileNode\n");

        indentLevel++;
        sb.append(indent()).append("Condition:\n");
        sb.append(node.condition.accept(this));

        sb.append(indent()).append("Do:\n");
        sb.append(node.body.accept(this));
        indentLevel--;

        return sb.toString();
    }

    @Override
    public String visitBinaryOpNode(BinaryOpNode node) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("BinaryOpNode: '").append(node.operator.lexeme).append("'\n");

        indentLevel++;
        sb.append(indent()).append("Left:\n");
        sb.append(node.left.accept(this));

        sb.append(indent()).append("Right:\n");
        sb.append(node.right.accept(this));
        indentLevel--;

        return sb.toString();
    }

    @Override
    public String visitIntLitNode(IntLitNode node) {
        return indent() + "IntLitNode: " + node.value.lexeme + "\n";
    }

    @Override
    public String visitBooleanLitNode(BooleanLitNode node) {
        return indent() + "BooleanLitNode: " + node.value.lexeme + "\n";
    }

    @Override
    public String visitVariableUseNode(VariableUseNode node) {
        return indent() + "VariableUseNode: " + node.identifier.lexeme + "\n";
    }
}