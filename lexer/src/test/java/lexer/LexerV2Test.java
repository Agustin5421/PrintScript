package lexer;

import factory.LexerFactory;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import token.Token;
import token.types.TokenSyntaxType;

public class LexerV2Test extends CommonLexerTests {
  private Lexer lexerV2;

  @BeforeEach
  public void setUp() {
    lexerV2 = LexerFactory.getLexer("1.1");
  }

  @Override
  protected Lexer getLexer() {
    return lexerV2;
  }

  @Test
  public void testIfStatement() {
    String input = "if (x) { println(\"Hello, world!\"); }";
    List<Token> tokens = new ArrayList<>();

    Lexer myLexer = getLexer().setInput(input);

    while (myLexer.hasNext()) {
      tokens.add(myLexer.next());
    }

    Assertions.assertEquals(11, tokens.size());
    Assertions.assertEquals(TokenSyntaxType.IF, tokens.get(0).type());
  }
}
