package formatter;

import formatter.factory.FormatterInitializer;

public class FormatterV1Test extends AbstractFormatterTest {
  @Override
  protected MainFormatter initFormatter(String jsonOptions, String formattedCode) {
    return FormatterInitializer.init(jsonOptions, formattedCode, "1.0");
  }
}
