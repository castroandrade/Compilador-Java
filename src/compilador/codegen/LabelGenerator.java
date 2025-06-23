package compilador.codegen;

/**
 * Classe auxiliar para gerar rótulos (labels) únicos para o código de máquina.
 * Ex: L0, L1, L2, ...
 */
public class LabelGenerator {
    private int nextLabel = 0;

    /**
     * Gera e retorna um novo rótulo único.
     * @return Uma string representando o novo rótulo.
     */
    public String newLabel() {
        return "L" + nextLabel++;
    }
}