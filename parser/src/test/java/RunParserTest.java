import factory.LexerFactory;
import factory.ParserFactory;
import lexer.Lexer;
import org.junit.jupiter.api.Test;
import parsers.Parser;

public class RunParserTest {

  @Test
  public void testV1() {
    Lexer lexer = LexerFactory.getLexer("1.0");
    Parser parser = ParserFactory.getParser("1.0");

    ParserTest tests = new ParserTest(parser, lexer);
    tests.runAllTests("1.0");
  }

  @Test
  public void testV2() {
    // Test case for the method parseStatement(List<Token> tokens) in class parsers.Parser
  }
}
