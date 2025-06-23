package compilador.ast;

import compilador.lexer.Token;
import compilador.visitor.Visitor;

/**
 * Nó da AST para um literal booleano (true, false).
 */
public class BooleanLitNode implements ASTNode {
    public final Token value;

    public BooleanLitNode(Token value) {
        this.value = value;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitBooleanLitNode(this);
    }
}