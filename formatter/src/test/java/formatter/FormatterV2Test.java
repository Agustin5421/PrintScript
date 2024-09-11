package formatter;

import formatter.factory.FormatterInitializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FormatterV2Test extends AbstractFormatterTest {
  @Override
  protected MainFormatter initFormatter(String jsonOptions, String formattedCode) {
    return FormatterInitializer.init(jsonOptions, formattedCode, "1.1");
  }

  @Override
  public String getJsonOptions() {
    return """
            {
              "colonRules": {
                "before": true,
                "after": true
              },
              "equalSpaces": true,
              "printLineBreaks": 1,
              "indentSize": 1
            }
            """;
  }

  @Override
  public String alternativeOptions() {
    return """
            {
              "colonRules": {
                "before": true,
                "after": false
              },
              "equalSpaces": false,
              "printLineBreaks": 2,
              "indentSize": 4
            }
            """;
  }

  // TODO : Add additional tests for the new formatter
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

  @Test
  public void completeFormattingTest() {
    String code =
        "if (true) { if(a){hola=2;} let name: string = \"Oliver\";} else {a=3; a=5; a=6;} const a: number = 5;";
    String formattedCode =
        """
                if (true) {
                	if (a) {
                		hola = 2;
                	}
                	let name : string = "Oliver";
                } else {
                	a = 3;
                	a = 5;
                	a = 6;
                }
                const a : number = 5;
                """;
    MainFormatter formatter = initFormatter(getJsonOptions(), code);
    Assertions.assertEquals(formattedCode, formatter.formatProgram());
  }
}
