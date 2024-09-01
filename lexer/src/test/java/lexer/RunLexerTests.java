package lexer;

import factory.LexerFactory;
import org.junit.jupiter.api.Test;

public class RunLexerTests {

  @Test
  public void RunTests() {
    Lexer lexer = LexerFactory.getLexer("1.0");
    LexerTest tests = new LexerTest(lexer);

    tests.runAllTests();
  }
}
