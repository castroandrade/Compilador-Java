package compilador;

import compilador.ast.ASTNode;
import compilador.lexer.Scanner;
import compilador.parser.Parser;
import compilador.visitor.ASTPrinter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        // Verifica se o caminho do arquivo foi fornecido como argumento
        if (args.length != 1) {
            System.err.println("Uso: java -jar meucompilador.jar <caminho_para_o_arquivo_fonte>");
            System.exit(1); // Encerra com código de erro
        }

        String filePath = args[0];
        System.out.println("Compilando o arquivo: " + filePath);
        System.out.println("---------------------------------------------------------");

        try {
            // Lê todo o conteúdo do arquivo para uma string
            String sourceCode = new String(Files.readAllBytes(Paths.get(filePath)));

            // Etapa 1: Análise Léxica
            Scanner scanner = new Scanner(sourceCode);

            // Etapa 2 e 3: Análise Sintática e Construção da AST
            Parser parser = new Parser(scanner);
            ASTNode astRoot = parser.parse(); // O método parse agora retorna a raiz da AST

            System.out.println("Análise sintática e construção da AST concluídas com sucesso.");
            System.out.println("---------------------------------------------------------");

            // Etapa 3: Visualização da AST
            System.out.println("Visualização da Árvore Sintática Abstrata (AST):");
            ASTPrinter printer = new ASTPrinter();
            String astVisual = astRoot.accept(printer);
            System.out.println(astVisual);

        } catch (IOException e) {
            // Erro específico para quando o arquivo não é encontrado ou não pode ser lido
            System.err.println("Erro de arquivo: Não foi possível ler o arquivo '" + filePath + "'");
        } catch (RuntimeException e) {
            // Erro específico para os erros de sintaxe que lançamos no Parser
            // Usar e.getMessage() exibe a mensagem de erro limpa que criamos
            System.err.println("Erro de compilação: " + e.getMessage());
        } catch (Exception e) {
            // Captura para qualquer outro erro inesperado
            System.err.println("Ocorreu um erro inesperado durante a compilação:");
            e.printStackTrace();
        }
    }
}