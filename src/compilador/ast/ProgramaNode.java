package compilador.ast;

import compilador.visitor.Visitor;
import java.util.List;

public class ProgramaNode implements ASTNode {
    public final List<ASTNode> declaracoes;
    public final ASTNode comandoComposto;

    public ProgramaNode(List<ASTNode> declaracoes, ASTNode comandoComposto) {
        this.declaracoes = declaracoes;
        this.comandoComposto = comandoComposto;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitProgramaNode(this);
    }
}