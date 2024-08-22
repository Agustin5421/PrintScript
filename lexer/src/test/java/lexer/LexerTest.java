package lexer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import exceptions.UnsupportedCharacter;
import java.util.List;
import org.junit.jupiter.api.Test;
import token.Position;
import token.Token;
import token.types.TokenTagType;
import token.types.TokenValueType;
import token.validators.DataTypeTokenChecker;
import token.validators.IdentifierTypeChecker;
import token.validators.OperationTypeTokenChecker;
import token.validators.TagTypeTokenChecker;
import token.validators.TokenTypeChecker;

public class LexerTest {

  @Test
  public void testLexer() {
    Lexer lexer = initLexer();

    assertEquals(
        13,
        lexer
            .extractTokens("2; let code = 9                     5 this is a VARIABLE VAR STRING")
            .size());
    assertEquals(5, lexer.extractTokens("string name = Olive;").size());
    assertEquals(5, lexer.extractTokens("string name = 190;").size());
    assertEquals(9, lexer.extractTokens("string name = 190+2+number;").size());

    List<Token> tokens = lexer.extractTokens("name = 'hello world + 2' + 'hola'" + "\n abc");

    List<Token> functionTest = lexer.extractTokens("println('Hello', 'World')");
    assertEquals(TokenTagType.IDENTIFIER, tokens.get(0).getType());
  }

  @Test
  public void testLexer1() {

    // Initialize the token type checkers
    Lexer lexer = initLexer();

    // Define the test cases
    String[] testCases = {"name = 2", "\"Hello World\" + 3.14", "\n abc", "'string' true 45"};

    // Run the test cases
    for (String testCase : testCases) {
      System.out.println("--------------------------------------------------------------------");
      List<Token> tokens = lexer.extractTokens(testCase);
      System.out.println("Input: " + testCase);
      tokens.forEach(token -> System.out.println("Token: " + token));
    }
    System.out.println("--------------------------------------------------------------------");
  }

  @Test
  public void testSeveralLinesCode() {
    Lexer lexer = initLexer();

    String code = "This\nis\nan\nexample\n'hi\nhey'";

    List<Token> tokens = lexer.extractTokens(code);

    List<Token> tokensToCompare =
        List.of(
            new Token(TokenTagType.IDENTIFIER, "This", new Position(1, 1), new Position(1, 5)),
            new Token(TokenTagType.IDENTIFIER, "is", new Position(2, 1), new Position(2, 3)),
            new Token(TokenTagType.IDENTIFIER, "an", new Position(3, 1), new Position(3, 3)),
            new Token(TokenTagType.IDENTIFIER, "example", new Position(4, 1), new Position(4, 8)),
            new Token(TokenValueType.STRING, "'hi\nhey'", new Position(5, 1), new Position(6, 5)));

    for (int i = 0; i < tokens.size(); i++) {
      Token tokenToComp = tokensToCompare.get(i);
      Token token = tokens.get(i);
      assertEquals(tokenToComp.getValue(), token.getValue());
      assertEquals(tokenToComp.getType(), token.getType());
      assertEquals(tokenToComp.getInitialPosition().row(), token.getInitialPosition().row());
      assertEquals(tokenToComp.getInitialPosition().col(), token.getInitialPosition().col());
      assertEquals(tokenToComp.getFinalPosition().row(), token.getFinalPosition().row());
      assertEquals(tokenToComp.getFinalPosition().col(), token.getFinalPosition().col());
    }
  }

  @Test
  public void testInvalidCase() {
    Lexer lexer = initLexer();
    String code = "!";

    //        assertThrows(Exception.class, () -> {
    //            lexer.extractTokens(code);
    //        });

    assertThrows(
        UnsupportedCharacter.class,
        () -> {
          lexer.extractTokens(code);
        });
  }

  @Test
  public void testOneBasedPosition() {
    Lexer lexer = initLexer();
    String code = "let";

    Token token = lexer.extractTokens(code).get(0);

    assertEquals(token.getInitialPosition().col(), 100);
  }

  private static Lexer initLexer() {
    TagTypeTokenChecker tagTypeChecker = new TagTypeTokenChecker();
    OperationTypeTokenChecker operationTypeChecker = new OperationTypeTokenChecker();
    DataTypeTokenChecker dataTypeChecker = new DataTypeTokenChecker();
    IdentifierTypeChecker identifierTypeChecker = new IdentifierTypeChecker();

    TokenTypeChecker tokenTypeChecker =
        new TokenTypeChecker(
            List.of(tagTypeChecker, operationTypeChecker, dataTypeChecker, identifierTypeChecker));

    return new Lexer(tokenTypeChecker);
  }
}
