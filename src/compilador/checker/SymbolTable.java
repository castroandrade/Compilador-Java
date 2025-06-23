package compilador.checker;

import java.util.HashMap;
import java.util.Map;

/**
 * Tabela de Símbolos para armazenar e consultar informações sobre identificadores.
 * Nesta implementação, usamos um único escopo global.
 */
public class SymbolTable {
    private final Map<String, IdEntry> table;

    public SymbolTable() {
        this.table = new HashMap<>();
    }

    /**
     * Adiciona uma nova entrada à tabela.
     * Lança um erro se o identificador já foi declarado.
     * @param entry A entrada do identificador a ser adicionada.
     */
    public void add(IdEntry entry) {
        String name = entry.token.lexeme;
        if (table.containsKey(name)) {
            // Regra de escopo: não permitir declarações duplicadas
            throw new Error("Erro de contexto: Variavel '" + name + "' ja declarada.");
        }
        table.put(name, entry);
    }

    /**
     * Procura por um identificador na tabela.
     * @param name O nome do identificador a ser procurado.
     * @return A IdEntry correspondente ou null se não for encontrada.
     */
    public IdEntry find(String name) {
        return table.get(name);
    }
}