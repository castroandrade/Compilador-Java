package Analisador.Lexico;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        // Código de exemplo baseado na sua gramática
        String sourceCode = "program TesteAtribuicao;\n" +
                "var\n" +
                "  idade : integer;\n" +
                "  ativo : boolean;\n" +
                "  salario : integer;\n" +
                "\n" +
                "begin\n" +
                "  idade := 30;\n" +
                "  ativo := true;\n" +
                "  salario := 5000;\n" +
                "end";

        System.out.println("Analisando o código-fonte:");
        System.out.println("---------------------------");
        System.out.println(sourceCode);
        System.out.println("---------------------------");
        System.out.println("Tokens Gerados:");

        // 1. Instanciar o Scanner
        Scanner scanner = new Scanner(sourceCode);

        // 2. Chamar o método para analisar e obter os tokens
        List<Token> tokens = scanner.scanTokens();

        // 3. Imprimir os tokens para ver o resultado
        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}