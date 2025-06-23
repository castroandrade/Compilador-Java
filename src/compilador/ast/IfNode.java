package compilador.ast;

import compilador.visitor.Visitor;

public class IfNode implements ASTNode {
    public final ASTNode condition;
    public final ASTNode thenBranch;
    public final ASTNode elseBranch; // Pode ser null

    public IfNode(ASTNode condition, ASTNode thenBranch, ASTNode elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitIfNode(this);
    }
}