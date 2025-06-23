package compilador.ast;

import compilador.visitor.Visitor;

/**
 * Nó da AST para a estrutura de repetição 'while'.
 */
public class WhileNode implements ASTNode {
    // A expressão que serve como condição do laço
    public final ASTNode condition;
    // O comando ou bloco de comandos a ser executado
    public final ASTNode body;

    public WhileNode(ASTNode condition, ASTNode body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitWhileNode(this);
    }
}