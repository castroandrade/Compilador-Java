package compilador.ast;

import compilador.lexer.Token;
import compilador.visitor.Visitor;

/**
 * Nó da AST que representa o uso de uma variável em uma expressão.
 */
public class VariableUseNode implements ASTNode {
    public final Token identifier;

    public VariableUseNode(Token identifier) {
        this.identifier = identifier;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitVariableUseNode(this);
    }
}