package formatter;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.root.Program;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.VariableDeclaration;
import com.google.gson.JsonSyntaxException;
import factory.LexerFactory;
import factory.ParserFactory;
import java.util.List;
import lexer.Lexer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parsers.Parser;
import token.Position;
import token.Token;

public class FormatterTest {
  private final MainFormatter formatter = MainFormatterInitializer.init();
  private final String jsonOptions =
      """
            {
              "rules": {
                "colonRules": {
                  "before": true,
                  "after": true
                },
                "equalSpaces": true,
                "printLineBreaks": 1
              }
            }
            """;
  private final Position defaultPosition = new Position(0, 0);

  @Test
  public void varDeclarationTest() {
    Identifier name = new Identifier("myVar     ", defaultPosition, defaultPosition);
    StringLiteral value = new StringLiteral("\"Hello World!\"", defaultPosition, defaultPosition);
    VariableDeclaration variableDeclaration = new VariableDeclaration(name, value);
    Program program = new Program(List.of(variableDeclaration));
    String formattedCode =
        """
                let myVar : string = "Hello World!";
                """;
    Assertions.assertEquals(formattedCode, formatter.format(program, jsonOptions));
  }

  @Test
  public void assignationTest() {
    Identifier name = new Identifier("   \n myVar     ", defaultPosition, defaultPosition);
    StringLiteral value = new StringLiteral("\"Goodbye World!\"", defaultPosition, defaultPosition);
    AssignmentExpression reAssignation = new AssignmentExpression(name, value, "=");
    Program program = new Program(List.of(reAssignation));
    String formattedCode = """
                myVar = "Goodbye World!";
                """;
    Assertions.assertEquals(formattedCode, formatter.format(program, jsonOptions));
  }

  @Test
  public void varDeclarationWithExpressionTest() {
    Identifier name = new Identifier("   \n myVar     ", defaultPosition, defaultPosition);
    NumberLiteral first = new NumberLiteral(2, defaultPosition, defaultPosition);
    NumberLiteral second = new NumberLiteral(2, defaultPosition, defaultPosition);
    NumberLiteral third = new NumberLiteral(5, defaultPosition, defaultPosition);
    BinaryExpression expression =
        new BinaryExpression(new BinaryExpression(first, second, "+"), third, "*");
    VariableDeclaration declaration = new VariableDeclaration(name, expression);
    Program program = new Program(List.of(declaration));
    String formattedCode = """
                let myVar : number = 2 + 2 * 5;
                """;
    Assertions.assertEquals(formattedCode, formatter.format(program, jsonOptions));
  }

  @Test
  public void printLnTest() {
    Identifier name = new Identifier("println", defaultPosition, defaultPosition);
    StringLiteral value = new StringLiteral("\"Hello World!\"", defaultPosition, defaultPosition);
    CallExpression printLnExpression = new CallExpression(name, List.of(value), false);
    Program program = new Program(List.of(printLnExpression));
    String formattedCode = """
                println("Hello World!");
                """;
    Assertions.assertEquals(formattedCode, formatter.format(program, jsonOptions));
  }

  @Test
  public void completeFormattingTest() {
    Parser parser = ParserFactory.getParser("1.0");
    Lexer lexer = LexerFactory.getLexer("1.0");
    List<Token> tokens =
        lexer.tokenize(
            """
                        let myVar     : number
                          = 2 + 3 * 2;
                        println(myVar     )    ;
                        myVar       ="Hello World!";
                        println(     myVar);
                        """);

    Program program = parser.parse(tokens);
    String formattedCode =
        """
                let myVar : number = 2 + 3 * 2;

                println(myVar);
                myVar = "Hello World!";

                println(myVar);
                """;
    Assertions.assertEquals(formattedCode, formatter.format(program, jsonOptions));
  }

  @Test
  public void differentFormatTest() {
    final String newJsonOptions =
        """
                {
                  "rules": {
                    "colonRules": {
                      "before": true,
                      "after": false
                    },
                    "equalSpaces": false,
                    "printLineBreaks": 2
                  }
                }
                """;

    Parser parser = ParserFactory.getParser("1.0");
    Lexer lexer = LexerFactory.getLexer("1.0");

    List<Token> tokens =
        lexer.tokenize(
            """
                        let myVar : number = 2 + 3 * 2;
                        println(myVar);
                        myVar = "Hello World!";
                        println(myVar);""");

    Program program = parser.parse(tokens);

    String formattedCode =
        """
                let myVar :number=2 + 3 * 2;


                println(myVar);
                myVar="Hello World!";


                println(myVar);
                """;
    Assertions.assertEquals(formattedCode, formatter.format(program, newJsonOptions));
  }

  @Test
  public void invalidOptionsTest() {
    Assertions.assertThrows(
        JsonSyntaxException.class,
        () -> {
          Program program =
              new Program(List.of(new Identifier("null", defaultPosition, defaultPosition)));
          formatter.format(program, "invalid options json");
        });
  }

  private static Lexer initLexer() {
    return LexerFactory.getLexer("1.0");
  }
}
