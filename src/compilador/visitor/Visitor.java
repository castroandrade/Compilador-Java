package compilador.visitor;

import compilador.ast.*;

public interface Visitor<T> {
    T visitProgramaNode(ProgramaNode node);
    T visitAssignNode(AssignNode node);
    T visitIfNode(IfNode node);
    T visitBinaryOpNode(BinaryOpNode node);
    T visitIntLitNode(IntLitNode node);
    T visitVarDeclNode(VarDeclNode node);
    T visitBeginEndNode(BeginEndNode node);
    T visitWhileNode(WhileNode node);
    T visitBooleanLitNode(BooleanLitNode node);
    T visitVariableUseNode(VariableUseNode node);
}