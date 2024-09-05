import factory.ParserFactory;
import org.junit.jupiter.api.BeforeEach;
import parsers.Parser;

public class LexerV2Test extends CommonParserTests {
  private Parser parserV2;

  @BeforeEach
  public void setUp() {
    parserV2 = ParserFactory.getParser("1.1");
  }

  @Override
  protected Parser getParser() {
    return parserV2;
  }

  /*
  @Test
  public void testIfStatement() {
    Parser parser = setParser("if (true) { println(2); } else { println(3); }", getParser());
    assertInstanceOf(IfStatement.class, parser.next());
  }

   */
}
