## Compilador Java (Linguagem Triangle → TAM)

Implementação educacional de um compilador em Java para uma linguagem do tipo Triangle, com geração de código para a TAM (Triangle Abstract Machine). O projeto inclui interface gráfica (Swing) para abrir um arquivo fonte, compilar e visualizar AST, mensagens de verificação semântica e o código objeto TAM.

### Funcionalidades

- **Análise léxica**: conversão do texto-fonte em tokens.
- **Análise sintática + AST**: construção da Árvore Sintática Abstrata.
- **Análise de contexto (semântica)**: checagem de declaração/uso de identificadores, tipos, escopos, etc.
- **Geração de código**: emissão de instruções TAM (lista de strings) a partir da AST.
- **GUI em Swing**: editor simples, botão de compilar, console de saída com resultados.

### Requisitos

- **Java**: JDK 11+ (funciona também em versões recentes do JDK 8+).
- Opcional: **IntelliJ IDEA** / **Eclipse** para executar com mais facilidade.

### Como executar

#### Via IDE (recomendado)

1. Importe o projeto como projeto Java.
2. Defina a classe principal `compilador.Main`.
3. Execute a aplicação; a GUI será aberta.

#### Via terminal (Windows PowerShell)

No diretório raiz do projeto:

1. Gere uma lista de fontes e compile:

```powershell
dir /s /b src\*.java > sources.txt
javac -encoding UTF-8 -d out @sources.txt
```

2. Execute a aplicação (GUI):

```powershell
java -cp out compilador.Main
```

### Uso da GUI

- Abra um arquivo-fonte pelo menu "Arquivo" → "Abrir Arquivo..." (ex.: arquivos em `casos_de_teste/`).
- Clique em "Compilar" para executar as etapas: léxica, sintática, contexto e geração de código.
- A área inferior exibe:
  - mensagens de sucesso/erro das etapas;
  - a AST impressa;
  - o código objeto TAM gerado.

### Casos de teste

O diretório `casos_de_teste/` contém exemplos:

- **teste_geral_valido.tam / .txt**: programa válido e seu resultado esperado.
- **erro\_\***: exemplos com erros léxicos/sintáticos/semânticos (ponto e vírgula, tipos, escopo, etc.).

### Estrutura do projeto

- `src/compilador/`:
  - `lexer/`: `Scanner`, `Token`, `TokenType`.
  - `parser/`: `Parser` que constrói a AST.
  - `ast/`: nós da AST (ex.: `ProgramaNode`, `IfNode`, `WhileNode`, etc.).
  - `visitor/`: visitantes como `ASTPrinter`, `CheckerVisitor`, `CodeGenVisitor`.
  - `checker/`: tabela de símbolos (`SymbolTable`, `IdEntry`).
  - `codegen/`: utilidades de geração, ex.: `LabelGenerator`.
  - `CompilerGUI`: interface gráfica Swing.
  - `Main`: ponto de entrada que inicializa a GUI.
- `casos_de_teste/`: arquivos de entrada e saídas esperadas.
- `out/`: classes compiladas quando você compila pelo terminal.

### Observações

- O foco é didático. Nem todos os recursos da linguagem Triangle estão implementados.
- O código gerado (TAM) é exibido como texto; a execução em uma VM TAM não faz parte deste projeto.
