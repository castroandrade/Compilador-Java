package compilador.ast;

import compilador.lexer.Token;
import compilador.visitor.Visitor;

public class AssignNode implements ASTNode {
    public final Token variable;
    public final ASTNode expression;

    public AssignNode(Token variable, ASTNode expression) {
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitAssignNode(this);
    }
}