package compilador.ast;

import compilador.lexer.Token;
import compilador.visitor.Visitor;

public class IntLitNode implements ASTNode {
    public final Token value;

    public IntLitNode(Token value) {
        this.value = value;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitIntLitNode(this);
    }
}