package compilador.visitor;

import compilador.ast.*;
import compilador.codegen.LabelGenerator;
import compilador.lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

/**
 * Visitor para percorrer a AST e gerar o código de máquina para a TAM.
 * Constrói uma lista de instruções em formato de string.
 */
public class CodeGenVisitor implements Visitor<Void> {

    private final List<String> objectCode;
    private final LabelGenerator labelGenerator;

    public CodeGenVisitor() {
        this.objectCode = new ArrayList<>();
        this.labelGenerator = new LabelGenerator();
    }

    /**
     * Retorna a lista de instruções geradas.
     * @return O código-objeto como uma lista de strings.
     */
    public List<String> getObjectCode() {
        return this.objectCode;
    }

    private void emit(String instruction) {
        objectCode.add(instruction);
    }

    // --- MÉTODOS DE VISITA ---

    @Override
    public Void visitProgramaNode(ProgramaNode node) {
        // Visita as declarações primeiro (no futuro, para alocar memória)
        for (ASTNode decl : node.declaracoes) {
            decl.accept(this);
        }
        // Visita o corpo principal do programa
        node.comandoComposto.accept(this);
        // Adiciona a instrução final para parar a máquina
        emit("HALT");
        return null;
    }

    @Override
    public Void visitVarDeclNode(VarDeclNode node) {
        // Na Etapa 5 simplificada, não geramos código para declarações,
        // apenas as usamos na análise de contexto.
        // Em uma versão avançada, geraríamos "PUSH 1" para alocar espaço.
        return null;
    }

    @Override
    public Void visitBeginEndNode(BeginEndNode node) {
        for (ASTNode cmd : node.commands) {
            cmd.accept(this);
        }
        return null;
    }

    @Override
    public Void visitAssignNode(AssignNode node) {
        // 1. Gera código para a expressão à direita. O resultado ficará no topo da pilha.
        node.expression.accept(this);
        // 2. Gera a instrução para armazenar o valor do topo da pilha na variável.
        // Conforme a especificação, as variáveis são referenciadas por nome.
        emit("STORE " + node.variable.lexeme);
        return null;
    }

    @Override
    public Void visitIfNode(IfNode node) {
        String elseLabel = labelGenerator.newLabel();
        String endIfLabel = labelGenerator.newLabel();

        // 1. Gera código para a condição. O resultado (0 ou 1) ficará na pilha.
        node.condition.accept(this);
        // 2. Se a condição for falsa (0), pula para o rótulo do "else".
        emit("JUMPIF(0) " + elseLabel);

        // 3. Gera código para o bloco "then".
        node.thenBranch.accept(this);
        // 4. Pula incondicionalmente para o fim do "if" para não executar o "else".
        emit("JUMP " + endIfLabel);

        // 5. Emite o rótulo do "else".
        emit(elseLabel + ":");
        if (node.elseBranch != null) {
            node.elseBranch.accept(this);
        }

        // 6. Emite o rótulo de fim do "if".
        emit(endIfLabel + ":");
        return null;
    }

    @Override
    public Void visitWhileNode(WhileNode node) {
        String startLabel = labelGenerator.newLabel();
        String endLabel = labelGenerator.newLabel();

        // 1. Emite o rótulo do início do laço.
        emit(startLabel + ":");
        // 2. Gera código para a condição.
        node.condition.accept(this);
        // 3. Se a condição for falsa (0), pula para o fim do laço.
        emit("JUMPIF(0) " + endLabel);
        // 4. Gera código para o corpo do laço.
        node.body.accept(this);
        // 5. Pula de volta para o início para reavaliar a condição.
        emit("JUMP " + startLabel);
        // 6. Emite o rótulo de fim do laço.
        emit(endLabel + ":");
        return null;
    }

    @Override
    public Void visitBinaryOpNode(BinaryOpNode node) {
        // 1. Gera código para o operando esquerdo.
        node.left.accept(this);
        // 2. Gera código para o operando direito.
        node.right.accept(this);
        // 3. Emite a instrução da operação, que consumirá os dois valores do topo
        //    da pilha e empilhará o resultado.
        switch (node.operator.type) {
            case PLUS: emit("ADD"); break;
            case MINUS: emit("SUB"); break;
            case TIMES: emit("MULT"); break;
            case DIV: emit("DIV"); break;
            case AND: emit("AND"); break;
            case OR: emit("OR"); break;
            case EQ: emit("EQ"); break;
            case LT: emit("LT"); break;
            case GT: emit("GT"); break;
        }
        return null;
    }

    @Override
    public Void visitVariableUseNode(VariableUseNode node) {
        emit("LOAD " + node.identifier.lexeme);
        return null;
    }

    @Override
    public Void visitIntLitNode(IntLitNode node) {
        emit("LOADL " + node.value.lexeme);
        return null;
    }

    @Override
    public Void visitBooleanLitNode(BooleanLitNode node) {
        // Representamos 'true' como 1 e 'false' como 0 na TAM
        if (node.value.type == TokenType.TRUE) {
            emit("LOADL 1");
        } else {
            emit("LOADL 0");
        }
        return null;
    }
}