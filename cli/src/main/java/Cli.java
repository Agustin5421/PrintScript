import ast.root.Program;
import formatter.MainFormatter;
import formatter.MainFormatterInitializer;
import interpreter.Interpreter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lexer.Lexer;
import linter.Linter;
import observers.ProgressObserver;
import observers.ProgressPrinter;
import parsers.Parser;
import token.Token;
import token.validators.*;
import token.validators.SyntaxTokenChecker;

public class Cli {
  private static final Logger logger = Logger.getLogger(Cli.class.getName());

  private static void executeFile(String code) {
    ProgressPrinter progressPrinter = new ProgressPrinter();
    ProgressObserver observer = new ProgressObserver(progressPrinter, 3);

    Lexer lexer = initLexer(observer);
    Parser parser = new Parser(List.of(observer));
    Interpreter interpreter = new Interpreter(List.of(observer));

    executeProgram(lexer, code, observer, parser, interpreter);
  }

  private static void executeProgram(
      Lexer lexer,
      String content,
      ProgressObserver observer,
      Parser parser,
      Interpreter interpreter) {
    try {
      List<Token> tokens = lexer.extractTokens(content);

      Program program = parser.parse(tokens);

      interpreter.executeProgram(program);

      observer.finish();
    } catch (Exception e) {
      observer.error();
      System.out.println();
      System.out.println(e.getMessage());
    }
  }

  // .\gradlew :cli:run --args="Validation src/main/resources/clitest.txt"
  // .\gradlew :cli:run --args="Execution src/main/resources/clitest.txt"
  // .\gradlew :cli:run --args="Formatter src/main/resources/clitest.txt
  // src/main/resources/formatterOptionsTest.json"
  // .\gradlew :cli:run --args="Analyzing src/main/resources/clitest.txt
  // src/main/resources/linterOptionsTest.json"
  public static void main(String[] args) {
    if (args.length == 0) {
      System.out.println("Please enter a valid instruction");
      return;
    }

    String operation = args[0];
    String codeFilePath = args[1];

    String code = getText(codeFilePath);

    switch (operation) {
      case "Validation" -> validateFile(code, new ProgressObserver(new ProgressPrinter(), 2));
      case "Execution" -> executeFile(code);
      case "Analyzing" -> analyzeFile(code, args[2]);
      case "Formatting" -> formatFile(codeFilePath, args[2]);
      default -> throw new IllegalArgumentException("Unsupported operation: " + operation);
    }
  }

  private static void formatFile(String codeFilePath, String optionsFilePath) {
    String code = getText(codeFilePath);
    String options = getText(optionsFilePath);

    ProgressObserver observer = new ProgressObserver(new ProgressPrinter(), 3);

    MainFormatter formatter = MainFormatterInitializer.init(observer);

    Program program = validateFile(code, observer);

    assert program != null;
    String formattedCode = formatter.format(program, options);
    writeToFile(formattedCode, codeFilePath);
    observer.finish();
  }

  private static void analyzeFile(String code, String filepath) {
    ProgressObserver observer = new ProgressObserver(new ProgressPrinter(), 3);

    String options = getText(filepath);
    Program program = validateFile(code, observer);

    Linter linter = new Linter(List.of(observer));
    try {
      assert program != null;
      linter.linter(program, options);
      observer.finish();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      observer.error();
    }
  }

  private static Program validateFile(String code, ProgressObserver observer) {
    Lexer lexer = initLexer(observer);
    Parser parser = new Parser(List.of(observer));

    try {
      return parser.parse(lexer.extractTokens(code));
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  private static String getText(String filePath) {
    StringBuilder content = new StringBuilder();

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = br.readLine()) != null) {
        content.append(line).append(System.lineSeparator());
      }
    } catch (IOException e) {
      System.out.println("An error occurred reading the file: " + e.getMessage());
    }
    return content.toString();
  }

  private static Lexer initLexer(ProgressObserver observer) {
    SyntaxTokenChecker tagTypeChecker = new SyntaxTokenChecker();
    OperationTypeTokenChecker operationTypeChecker = new OperationTypeTokenChecker();
    DataTypeTokenChecker dataTypeChecker = new DataTypeTokenChecker();
    IdentifierTypeChecker identifierTypeChecker = new IdentifierTypeChecker();

    TokenTypeChecker tokenTypeChecker =
        new TokenTypeChecker(
            List.of(tagTypeChecker, operationTypeChecker, dataTypeChecker, identifierTypeChecker));

    return new Lexer(tokenTypeChecker, List.of(observer));
  }

  private static void writeToFile(String text, String filepath) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
      writer.write(text);
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Failed to write to file: " + filepath, e);
    }
  }
}
