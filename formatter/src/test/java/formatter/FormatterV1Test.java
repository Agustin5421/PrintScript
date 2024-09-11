package formatter;

import formatter.factory.FormatterInitializer;

public class FormatterV1Test extends AbstractFormatterTest {
  @Override
  protected MainFormatter initFormatter(String jsonOptions, String formattedCode) {
    return FormatterInitializer.init(jsonOptions, formattedCode, "1.0");
  }

  // TODO: add these tests after figuring out who handles the error (parser or formatter)
  /*
  @Test
  public void testInvalidBoolean() {
    String formattedCode = """
                let anotherVar : boolean = true;
                """;
    MainFormatter formatter = initFormatter(getJsonOptions(), formattedCode);
    assertThrows(UnsupportedExpressionException.class, formatter::formatProgram);
  }
  @Test
  public void testInvalidConst() {
    String formattedCode =
        """
                const anotherVar : string = "Hello World";
                """;
    MainFormatter formatter = initFormatter(getJsonOptions(), formattedCode);
    assertThrows(UnsupportedExpressionException.class, formatter::formatProgram);
  }

   */
}
