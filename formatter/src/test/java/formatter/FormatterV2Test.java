package formatter;

import formatter.factory.FormatterInitializer;

public class FormatterV2Test extends AbstractFormatterTest {
  @Override
  protected MainFormatter initFormatter(String jsonOptions, String formattedCode) {
    return FormatterInitializer.init(jsonOptions, formattedCode, "1.1");
  }

  // TODO : Add additional tests for the new formatter
  /*
  @Test
  public void additionalTest1() {
    String formattedCode = """
                let anotherVar : boolean = true;
                """;
    MainFormatter formatter = initFormatter(getJsonOptions(), formattedCode);
    Assertions.assertEquals(formattedCode, formatter.formatProgram());
  }

  @Test
  public void additionalTest2() {
    String formattedCode = """
                anotherVar = false;
                """;
    MainFormatter formatter = initFormatter(getJsonOptions(), formattedCode);
    Assertions.assertEquals(formattedCode, formatter.formatProgram());
  }

   */
}
