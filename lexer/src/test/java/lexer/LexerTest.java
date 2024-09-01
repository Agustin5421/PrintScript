package lexer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import exceptions.UnsupportedCharacter;
import java.util.List;
import org.junit.jupiter.api.Test;
import token.Token;
import token.types.TokenDataType;
import token.types.TokenSyntaxType;

public class LexerTest {
  private final Lexer lexer;

  public LexerTest(Lexer lexer) {
    this.lexer = lexer;
  }

  @Test
  public void testVarDeclaration() {
    String input = "let myVar string : x = 5;";
    List<Token> tokens = lexer.tokenize(input);
    assertEquals(8, tokens.size());
    assertEquals(TokenSyntaxType.DECLARATION, tokens.get(0).getType());
    assertEquals(TokenSyntaxType.IDENTIFIER, tokens.get(1).getType());
    assertEquals(TokenDataType.STRING_TYPE, tokens.get(2).getType());
    assertEquals(TokenSyntaxType.COLON, tokens.get(3).getType());
    assertEquals(TokenSyntaxType.IDENTIFIER, tokens.get(4).getType());
  }

  @Test
  public void testFunctionDeclaration() {
    String input = "function add(a) { return a + b; }";
    List<Token> tokens = lexer.tokenize(input);
    assertEquals(12, tokens.size());
  }

  @Test
  public void testPrint() {
    String input = "println(\"Hello, world!\");";
    List<Token> tokens = lexer.tokenize(input);
    assertEquals(5, tokens.size());
  }

  @Test
  public void testStringLiteral() {
    String input = "let greeting = \"Hello, world!\";";
    List<Token> tokens = lexer.tokenize(input);
    assertEquals(5, tokens.size());
  }

  @Test
  public void testNumberLiteral() {
    String input = "let greeting = 5;";
    List<Token> tokens = lexer.tokenize(input);
    assertEquals(5, tokens.size());
  }

  @Test
  public void testBinaryOperation() {
    String input = "let greeting = 5 + 5;";
    List<Token> tokens = lexer.tokenize(input);
    assertEquals(7, tokens.size());
  }

  @Test
  public void testUnsupportedChar() {
    String input = "let greeting = a,;";
    assertThrows(UnsupportedCharacter.class, () -> lexer.tokenize(input));
  }

  @Test
  public void runAllTests() {
    testVarDeclaration();
    testFunctionDeclaration();
    testPrint();
    testStringLiteral();
    testNumberLiteral();
    testBinaryOperation();
    testUnsupportedChar();
  }
}
