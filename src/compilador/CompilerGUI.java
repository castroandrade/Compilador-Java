package compilador;

import compilador.ast.ASTNode;
import compilador.lexer.Scanner;
import compilador.parser.Parser;
import compilador.visitor.ASTPrinter;
import compilador.visitor.CheckerVisitor;
import compilador.visitor.CodeGenVisitor;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

/**
 * Implementa uma interface gráfica (GUI) para o compilador usando Java Swing.
 * Permite ao usuário abrir um arquivo de código-fonte, visualizá-lo,
 * compilar e ver os resultados (AST, mensagens de erro, etc.) em uma console.
 */
public class CompilerGUI extends JFrame {

    private final JTextArea sourceCodeArea;
    private final JTextArea consoleArea;
    private File currentFile;

    public CompilerGUI() {
        super("Compilador da Linguagem Triangle");

        // Configuração da janela principal
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null); // Centraliza a janela

        // --- Componentes da UI ---

        // 1. Área de texto para o código-fonte com barra de rolagem
        sourceCodeArea = new JTextArea();
        sourceCodeArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane sourceScrollPane = new JScrollPane(sourceCodeArea);

        // 2. Área de texto para a console com barra de rolagem
        consoleArea = new JTextArea();
        consoleArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        consoleArea.setEditable(false);
        JScrollPane consoleScrollPane = new JScrollPane(consoleArea);

        // 3. Painel dividido para as áreas de texto
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sourceScrollPane, consoleScrollPane);
        splitPane.setResizeWeight(0.65); // Dá mais espaço para a área de código

        // 4. Botão de compilação
        JButton compileButton = new JButton("Compilar");
        compileButton.addActionListener(e -> compile());

        // 5. Barra de Menu
        createMenuBar();

        // --- Layout ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(compileButton);

        setLayout(new BorderLayout());
        add(splitPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Torna a janela visível
        setVisible(true);
    }

    /**
     * Cria e configura a barra de menu.
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Arquivo");

        // Item "Abrir"
        JMenuItem openItem = new JMenuItem("Abrir Arquivo...");
        openItem.addActionListener(e -> openFile());
        fileMenu.add(openItem);

        // Item "Sair"
        JMenuItem exitItem = new JMenuItem("Sair");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    /**
     * Abre um seletor de arquivos para carregar o código-fonte.
     */
    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            try {
                String sourceCode = new String(Files.readAllBytes(currentFile.toPath()));
                sourceCodeArea.setText(sourceCode);
                setTitle("Compilador - " + currentFile.getName());
            } catch (Exception e) {
                consoleArea.setText("Erro ao ler o arquivo: " + e.getMessage());
            }
        }
    }

    /**
     * Orquestra todas as etapas de compilação.
     * Este método é o coração da integração da GUI com o backend do compilador.
     */
    private void compile() {
        String sourceCode = sourceCodeArea.getText();
        if (sourceCode.isEmpty()) {
            consoleArea.setText("Nenhum codigo-fonte para compilar.");
            return;
        }

        consoleArea.setText(""); // Limpa a console
        consoleArea.append("Iniciando compilacao...\n");
        consoleArea.append("---------------------------------------------------------\n");

        try {
            // Etapa 1: Análise Léxica
            Scanner scanner = new Scanner(sourceCode);

            // Etapa 2 e 3: Análise Sintática e Construção da AST
            Parser parser = new Parser(scanner);
            ASTNode astRoot = parser.parse();
            consoleArea.append("[SUCESSO] Analise sintatica concluida. AST construida.\n");

            // Etapa 4: Análise de Contexto
            CheckerVisitor checker = new CheckerVisitor();
            astRoot.accept(checker);
            consoleArea.append("[SUCESSO] Analise de contexto concluida. O programa esta correto.\n");

            // Etapa 5: Geração de Código
            CodeGenVisitor codeGen = new CodeGenVisitor();
            astRoot.accept(codeGen);
            List<String> objectCode = codeGen.getObjectCode();
            consoleArea.append("[SUCESSO] Geracao de codigo concluida.\n\n");

            // Exibe o código gerado na console
            consoleArea.append("--- Codigo Objeto (TAM) ---\n");
            for (String instruction : objectCode) {
                consoleArea.append(instruction + "\n");
            }

            // Opcional: Imprimir a AST para verificação
            consoleArea.append("\n--- Arvore Sintatica Abstrata (AST) ---\n");
            ASTPrinter printer = new ASTPrinter();
            consoleArea.append(astRoot.accept(printer));


        } catch (Error | Exception e) {
            // Captura qualquer erro (sintático, de contexto, etc.) e exibe na console
            consoleArea.append("\nERRO DE COMPILACAO:\n" + e.getMessage());
        }
    }
}