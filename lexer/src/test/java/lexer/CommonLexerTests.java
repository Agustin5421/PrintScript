package lexer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import exceptions.UnsupportedCharacter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import token.Token;
import token.types.TokenDataType;
import token.types.TokenSyntaxType;
import token.types.TokenValueType;

public abstract class CommonLexerTests {
  protected abstract Lexer getLexer();

  private Lexer createLexer(String input) throws IOException {
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    return getLexer().setInput(inputStream);
  }

  @Test
  public void testVarDeclaration() throws IOException {
    String input = "let myVar string : x = 5;";
    List<Token> tokens = new ArrayList<>();

    Lexer myLexer = createLexer(input);
    while (myLexer.hasNext()) {
      tokens.add(myLexer.next());
    }

    assertEquals(8, tokens.size());
    assertEquals(TokenSyntaxType.LET_DECLARATION, tokens.get(0).tokenType());
    assertEquals(TokenSyntaxType.IDENTIFIER, tokens.get(1).tokenType());
    assertEquals(TokenDataType.STRING_TYPE, tokens.get(2).tokenType());
    assertEquals(TokenSyntaxType.COLON, tokens.get(3).tokenType());
    assertEquals(TokenSyntaxType.IDENTIFIER, tokens.get(4).tokenType());
    assertEquals(TokenSyntaxType.ASSIGNATION, tokens.get(5).tokenType());
    assertEquals(TokenValueType.NUMBER, tokens.get(6).tokenType());
  }

  @Test
  public void testFunctionDeclaration() {
    String input = "function add(a) ! return a + b;";
    assertThrows(
        UnsupportedCharacter.class,
        () -> {
          Lexer lexer = createLexer(input);
          while (lexer.hasNext()) {
            lexer.next();
          }
        });
  }

  @Test
  public void testPrint() throws IOException {
    String input = "println(\"Hello, world!\");";
    List<Token> tokens = new ArrayList<>();

    Lexer myLexer = createLexer(input);
    while (myLexer.hasNext()) {
      tokens.add(myLexer.next());
    }

    assertEquals(5, tokens.size());
  }

  @Test
  public void testStringLiteral() throws IOException {
    String input = "let greeting = \"Hello, world!\";";
    List<Token> tokens = new ArrayList<>();

    Lexer myLexer = createLexer(input);
    while (myLexer.hasNext()) {
      tokens.add(myLexer.next());
    }

    assertEquals(5, tokens.size());
    assertEquals(TokenValueType.STRING, tokens.get(3).tokenType());
  }

  @Test
  public void testNumberLiteral() throws IOException {
    String input = "let greeting = 5;";
    List<Token> tokens = new ArrayList<>();

    Lexer myLexer = createLexer(input);
    while (myLexer.hasNext()) {
      tokens.add(myLexer.next());
    }

    assertEquals(5, tokens.size());
    assertEquals(TokenValueType.NUMBER, tokens.get(3).tokenType());
  }

  @Test
  public void testBinaryOperation() throws IOException {
    String input = "let greeting = 5 + 5;";
    List<Token> tokens = new ArrayList<>();

    Lexer myLexer = createLexer(input);
    while (myLexer.hasNext()) {
      tokens.add(myLexer.next());
    }

    assertEquals(7, tokens.size());
    assertEquals(TokenDataType.OPERAND, tokens.get(4).tokenType());
  }
}
