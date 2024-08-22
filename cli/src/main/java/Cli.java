import ast.root.Program;
import formatter.MainFormatter;
import formatter.MainFormatterInitializer;
import interpreter.Interpreter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import lexer.Lexer;
import linter.Linter;
import linter.LinterInitializer;
import observers.ProgressObserver;
import observers.ProgressPrinter;
import parsers.Parser;
import token.Token;
import token.validators.DataTypeTokenChecker;
import token.validators.IdentifierTypeChecker;
import token.validators.OperationTypeTokenChecker;
import token.validators.TagTypeTokenChecker;
import token.validators.TokenTypeChecker;

public class Cli {
  public static void executeFile(String code) {
    ProgressPrinter progressPrinter = new ProgressPrinter();
    ProgressObserver observer = new ProgressObserver(progressPrinter);

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

  public static void main(String[] args) {
    /* if (args.length == 0) {
        System.out.println("Please enter a valid instruction");
        return;
    } */

    String operation = "Formatting";       // args[0];
    String filePath = "CLI/clitest.txt";  // args[1];

    String code = getCode(filePath);

    switch (operation) {
        case "Validation" -> validateFile(code);
        case "Execution" ->  executeFile(code);
        case "Analyzing" ->  {
          String options = "{}";  // args[2];
          Program program = validateFile(code);
          analyzeFile(program, options);
        }
        case "Formatting" -> {
          String options = """
                  {
                    "rules": {
                      "colonRules": {
                        "before": true,
                        "after": true
                      },
                      "equalSpaces": true,
                      "printLineBreaks": 1
                    }
                  }""";  // args[2];
          Program program = validateFile(code);
          formatFile(program, options);
        }
        default -> throw new IllegalArgumentException("Unsupported operation: " + operation);
    }
  }

  private static void formatFile(Program code, String options) {
    MainFormatter formatter = MainFormatterInitializer.init();
    String formattedCode = formatter.format(code, options);

    //TODO: Write formatted code to file
  }

  private static void analyzeFile(Program code, String options) {
    Linter linter = LinterInitializer.initLinter();
    try {
      linter.linter(code, options);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private static Program validateFile(String code) {
    ProgressPrinter progressPrinter = new ProgressPrinter();
    ProgressObserver observer = new ProgressObserver(progressPrinter);

    Lexer lexer = initLexer(observer);
    Parser parser = new Parser(List.of(observer));

    try {
      return parser.parse(lexer.extractTokens(code));
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  private static String getCode(String filePath) {
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
    TagTypeTokenChecker tagTypeChecker = new TagTypeTokenChecker();
    OperationTypeTokenChecker operationTypeChecker = new OperationTypeTokenChecker();
    DataTypeTokenChecker dataTypeChecker = new DataTypeTokenChecker();
    IdentifierTypeChecker identifierTypeChecker = new IdentifierTypeChecker();

    TokenTypeChecker tokenTypeChecker =
        new TokenTypeChecker(
            List.of(tagTypeChecker, operationTypeChecker, dataTypeChecker, identifierTypeChecker));

    return new Lexer(tokenTypeChecker, List.of(observer));
  }
}
