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

  public void runAllTests(String version) {
    switch (version) {
      case "1.0" -> runAllTestsV1();
      case "1.1" -> runAllTestsV2();
      default -> throw new IllegalArgumentException("Invalid version");
    }
  }

  private void runAllTestsV2() {
    runAllTestsV1();
    testIfStatement();
  }

  private void runAllTestsV1() {
    testVarDeclaration();
    testFunctionDeclaration();
    testPrint();
    testStringLiteral();
    testNumberLiteral();
    testBinaryOperation();
    testUnsupportedChar();
  }

  // Tests for version 1.0
  @Test
  public void testVarDeclaration() {
    String input = "let myVar string : x = 5;";
    List<Token> tokens = lexer.tokenize(input);
    assertEquals(8, tokens.size());
    assertEquals(TokenSyntaxType.DECLARATION, tokens.get(0).type());
    assertEquals(TokenSyntaxType.IDENTIFIER, tokens.get(1).type());
    assertEquals(TokenDataType.STRING_TYPE, tokens.get(2).type());
    assertEquals(TokenSyntaxType.COLON, tokens.get(3).type());
    assertEquals(TokenSyntaxType.IDENTIFIER, tokens.get(4).type());
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


  // Tests for version 1.1
  @Test
  public void testIfStatement() {
    String input = "if (a) { return a; } else { return b; }";
    List<Token> tokens = lexer.tokenize(input);
    assertEquals(15, tokens.size());
  }
}
