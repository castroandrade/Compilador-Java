package compilador;

import compilador.ast.ASTNode;
import compilador.lexer.Scanner;
import compilador.parser.Parser;
import compilador.visitor.ASTPrinter;
import compilador.visitor.CheckerVisitor; // Importe o novo visitor
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Uso: java -jar meucompilador.jar <caminho_para_o_arquivo_fonte>");
            System.exit(1);
        }

        String filePath = args[0];
        System.out.println("Compilando o arquivo: " + filePath);
        System.out.println("---------------------------------------------------------");

        try {
            String sourceCode = new String(Files.readAllBytes(Paths.get(filePath)));

            // 1. Análise Léxica
            Scanner scanner = new Scanner(sourceCode);

            // 2. Análise Sintática e Construção da AST
            Parser parser = new Parser(scanner);
            ASTNode astRoot = parser.parse();
            System.out.println("Sintaxe verificada com sucesso. AST construida.");

            // 3. Análise de Contexto
            CheckerVisitor checker = new CheckerVisitor();
            astRoot.accept(checker); // Percorre a AST aplicando as regras de contexto
            System.out.println("Analise de contexto finalizada com sucesso. O programa esta correto.");

            // 4. Visualização da AST
            System.out.println("---------------------------------------------------------");
            System.out.println("Visualizacao da Arvore Sintatica Abstrata (AST):");
            ASTPrinter printer = new ASTPrinter();
            String astVisual = astRoot.accept(printer);
            System.out.println(astVisual);

        } catch (IOException e) {
            System.err.println("Erro de arquivo: " + e.getMessage());
        } catch (Error e) {
            // Captura tanto os erros de sintaxe do Parser quanto os de contexto do Checker
            System.err.println("ERRO DE COMPILACAO: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ocorreu um erro inesperado durante a compilacao:");
            e.printStackTrace();
        }
    }
}