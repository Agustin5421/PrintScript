package lexer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import exceptions.UnsupportedCharacter;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import token.Token;
import token.types.TokenDataType;
import token.types.TokenSyntaxType;
import token.types.TokenValueType;

public abstract class CommonLexerTests {
  protected abstract Lexer getLexer();

  @Test
  public void testVarDeclaration() {
    String input = "let myVar string : x = 5;";
    List<Token> tokens = new ArrayList<>();

    Lexer myLexer = getLexer().setInput(input);
    while (myLexer.hasNext()) {
      tokens.add(myLexer.next());
    }

    assertEquals(8, tokens.size());
    assertEquals(TokenSyntaxType.DECLARATION, tokens.get(0).type());
    assertEquals(TokenSyntaxType.IDENTIFIER, tokens.get(1).type());
    assertEquals(TokenDataType.STRING_TYPE, tokens.get(2).type());
    assertEquals(TokenSyntaxType.COLON, tokens.get(3).type());
    assertEquals(TokenSyntaxType.IDENTIFIER, tokens.get(4).type());
    assertEquals(TokenSyntaxType.ASSIGNATION, tokens.get(5).type());
    assertEquals(TokenValueType.NUMBER, tokens.get(6).type());
  }

  @Test
  public void testFunctionDeclaration() {
    String input = "function add(a) ! return a + b;";
    assertThrows(
        UnsupportedCharacter.class,
        () -> {
          getLexer().tokenize(input);
        });
  }

  @Test
  public void testPrint() {
    String input = "println(\"Hello, world!\");";
    List<Token> tokens = new ArrayList<>();

    Lexer myLexer = getLexer().setInput(input);
    while (myLexer.hasNext()) {
      tokens.add(myLexer.next());
    }

    assertEquals(5, tokens.size());
  }

  @Test
  public void testStringLiteral() {
    String input = "let greeting = \"Hello, world!\";";
    List<Token> tokens = new ArrayList<>();

    Lexer myLexer = getLexer().setInput(input);
    while (myLexer.hasNext()) {
      tokens.add(myLexer.next());
    }

    assertEquals(5, tokens.size());
    assertEquals(TokenValueType.STRING, tokens.get(3).type());
  }

  @Test
  public void testNumberLiteral() {
    String input = "let greeting = 5;";
    List<Token> tokens = new ArrayList<>();

    Lexer myLexer = getLexer().setInput(input);
    while (myLexer.hasNext()) {
      tokens.add(myLexer.next());
    }

    assertEquals(5, tokens.size());
    assertEquals(TokenValueType.NUMBER, tokens.get(3).type());
  }

  @Test
  public void testBinaryOperation() {
    String input = "let greeting = 5 + 5;";
    List<Token> tokens = new ArrayList<>();

    Lexer myLexer = getLexer().setInput(input);
    while (myLexer.hasNext()) {
      tokens.add(myLexer.next());
    }

    assertEquals(7, tokens.size());
    assertEquals(TokenDataType.OPERAND, tokens.get(4).type());
  }
}
