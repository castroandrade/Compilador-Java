package compilador.ast;

import compilador.visitor.Visitor;

public interface ASTNode {
    /**
     * Aceita um visitor, implementando o padrão de projeto Visitor.
     * @param visitor O visitor que irá operar sobre este nó.
     * @return O resultado da operação do visitor.
     */
    <T> T accept(Visitor<T> visitor);
}