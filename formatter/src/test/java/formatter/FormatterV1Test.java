package formatter;

import static org.junit.jupiter.api.Assertions.assertThrows;

import exceptions.UnsupportedExpressionException;
import formatter.factory.FormatterInitializer;
import org.junit.jupiter.api.Test;

public class FormatterV1Test extends AbstractFormatterTest {
  @Override
  protected MainFormatter initFormatter(String jsonOptions, String formattedCode) {
    return FormatterInitializer.init(jsonOptions, formattedCode, "1.0");
  }

  @Test
  public void testV2() {
    String formattedCode = """
                let anotherVar : boolean = true;
                """;
    MainFormatter formatter = initFormatter(getJsonOptions(), formattedCode);
    assertThrows(UnsupportedExpressionException.class, formatter::formatProgram);
  }
}
