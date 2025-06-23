package compilador.ast;

import compilador.visitor.Visitor;
import java.util.List;

/**
 * Nó da AST que representa um bloco de comandos compostos (begin...end).
 * Contém uma lista de nós de comando.
 */
public class BeginEndNode implements ASTNode {
    public final List<ASTNode> commands;

    public BeginEndNode(List<ASTNode> commands) {
        this.commands = commands;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitBeginEndNode(this);
    }
}