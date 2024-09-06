import factory.ParserFactory;
import org.junit.jupiter.api.BeforeEach;
import parsers.Parser;

public class ParserV1Test extends CommonParserTests {
  private Parser parserV1;

  @BeforeEach
  public void setUp() {
    parserV1 = ParserFactory.getParser("1.0");
  }

  @Override
  protected Parser getParser() {
    return parserV1;
  }
}
