package compilador.ast;

import compilador.lexer.Token;
import compilador.visitor.Visitor;

public class BinaryOpNode implements ASTNode {
    public final ASTNode left;
    public final Token operator;
    public final ASTNode right;

    public BinaryOpNode(ASTNode left, Token operator, ASTNode right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitBinaryOpNode(this);
    }
}