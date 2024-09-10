import static org.junit.Assert.assertThrows;

import exceptions.UnsupportedDataType;
import factory.ParserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

  @Test
  public void testDataTypeException() {
    Parser parser = setParser("let name : boolean = \"Oliver\";", getParser());
    assertThrows(UnsupportedDataType.class, parser::next);
  }
}
