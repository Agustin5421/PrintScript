package lexer;

import factory.LexerFactory;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;

public class LexerV1Test extends CommonLexerTests {
  private Lexer lexerV1;

  @BeforeEach
  public void setUp() throws IOException {
    lexerV1 = LexerFactory.getLexer("1.0");
  }

  @Override
  protected Lexer getLexer() {
    return lexerV1;
  }
}
