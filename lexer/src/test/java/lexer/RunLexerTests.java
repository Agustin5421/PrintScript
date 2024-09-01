package lexer;

import factory.LexerFactory;
import org.junit.jupiter.api.Test;

public class RunLexerTests {

  // Tests for version 1.0
  @Test
  public void runTestsV1() {
    Lexer lexer = LexerFactory.getLexer("1.0");
    LexerTest tests = new LexerTest(lexer);

    tests.runAllTests("1.0");
  }

  // Tests for version 1.1
  @Test
  public void runTestsV2() {
    Lexer lexer = LexerFactory.getLexer("1.1");
    LexerTest tests = new LexerTest(lexer);

    tests.runAllTests("1.1");
  }
}
