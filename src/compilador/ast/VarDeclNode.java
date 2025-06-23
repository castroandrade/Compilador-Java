package compilador.ast;

import compilador.lexer.Token;
import compilador.visitor.Visitor;

/**
 * Nó da AST que representa uma declaração de variável.
 * Ex: var idade : integer;
 */
public class VarDeclNode implements ASTNode {
    public final Token identifier;
    public final Token type;

    public VarDeclNode(Token identifier, Token type) {
        this.identifier = identifier;
        this.type = type;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitVarDeclNode(this);
    }
}