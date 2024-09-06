import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import ast.root.AstNode;
import ast.statements.IfStatement;
import factory.ParserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parsers.Parser;

public class ParserV2Test extends CommonParserTests {
  private Parser parserV2;

  @BeforeEach
  public void setUp() {
    parserV2 = ParserFactory.getParser("1.1");
  }

  @Override
  protected Parser getParser() {
    return parserV2;
  }

  @Test
  public void testIfStatement() {
    Parser parser =
        setParser(
            "if (true) { if(a){hola=2;} let name: string = \"Oliver\";} else {a=3; a=5; a=6;}",
            getParser());
    AstNode node = parser.next();
    assertInstanceOf(IfStatement.class, node);
  }
}
