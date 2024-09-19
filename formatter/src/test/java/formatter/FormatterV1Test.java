package formatter;

import static org.junit.jupiter.api.Assertions.assertThrows;

import exceptions.UnexpectedTokenException;
import exceptions.UnsupportedExpressionException;
import org.junit.jupiter.api.Test;
import runner.TestRunner;

public class FormatterV1Test extends AbstractFormatterTest {
  @Override
  protected TestRunner setRunner(String jsonOptions, String formattedCode) {
    return new TestRunner(jsonOptions, formattedCode, "1.0");
  }

  @Test
  public void testInvalidBoolean() {
    String formattedCode = """
                let anotherVar : boolean = true;
                """;
    TestRunner testRunner = setRunner(getJsonOptions(), formattedCode);
    assertThrows(UnsupportedExpressionException.class, testRunner::runFormatting);
  }

  @Test
  public void testInvalidConst() {
    String formattedCode =
        """
                const anotherVar : string = "Hello World";
                """;
    TestRunner testRunner = setRunner(getJsonOptions(), formattedCode);
    assertThrows(UnexpectedTokenException.class, testRunner::runFormatting);
  }
}
