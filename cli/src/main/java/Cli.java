import ast.root.Program;
import interpreter.Interpreter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import lexer.Lexer;
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
  // Testing githooks
  public void executeFile(String filePath) {
    File file = new File(filePath);

    validateFile(filePath, file);

    String content = getCode(filePath);

    ProgressPrinter progressPrinter = new ProgressPrinter();
    ProgressObserver observer = new ProgressObserver(progressPrinter);

    Lexer lexer = initLexer(observer);
    Parser parser = new Parser(List.of(observer));
    Interpreter interpreter = new Interpreter(List.of(observer));

    executeProgram(lexer, content, observer, parser, interpreter);
    System.out.println();
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
        System.out.println("Please enter a file.");
        return;
    } */

    String filePath = "CLI/clitest.txt"; // args[0];
    Cli fileExecutor = new Cli();
    fileExecutor.executeFile(filePath);
  }

  private static void validateFile(String filePath, File file) {
    if (!file.exists()) {
      System.out.println("File does not exist: " + filePath);
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
