package compilador;

import compilador.ast.ASTNode;
import compilador.lexer.Scanner;
import compilador.parser.Parser;
import compilador.visitor.ASTPrinter;
import compilador.visitor.CheckerVisitor;
import compilador.visitor.CodeGenVisitor; // Importe o novo visitor
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List; // Importe a classe List

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Uso: java -jar meucompilador.jar <caminho_para_o_arquivo_fonte>");
            System.exit(1);
        }

        String filePath = args[0];
        // Define o nome do arquivo de saída
        String outputFilePath = filePath.replaceFirst("[.][^.]+$", "") + ".tam";

        System.out.println("Compilando o arquivo: " + filePath);
        System.out.println("---------------------------------------------------------");

        try {
            String sourceCode = new String(Files.readAllBytes(Paths.get(filePath)));

            // 1. Análise Léxica
            Scanner scanner = new Scanner(sourceCode);

            // 2. Análise Sintática e Construção da AST
            Parser parser = new Parser(scanner);
            ASTNode astRoot = parser.parse();
            System.out.println("Sintaxe verificada com sucesso.");

            // 3. Análise de Contexto
            CheckerVisitor checker = new CheckerVisitor();
            astRoot.accept(checker);
            System.out.println("Analise de contexto finalizada com sucesso.");

            // 4. Geração de Código (A NOVA ETAPA)
            CodeGenVisitor codeGen = new CodeGenVisitor();
            astRoot.accept(codeGen);
            List<String> objectCode = codeGen.getObjectCode();

            // 5. Salva o código-objeto em um arquivo .tam
            Files.write(Paths.get(outputFilePath), objectCode);
            System.out.println("---------------------------------------------------------");
            System.out.println("Compilacao finalizada. Codigo-objeto gerado em: " + outputFilePath);


        } catch (IOException e) {
            System.err.println("Erro de arquivo: " + e.getMessage());
        } catch (Error e) {
            System.err.println("ERRO DE COMPILACAO: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado durante a compilacao:");
            e.printStackTrace();
        }
    }
}