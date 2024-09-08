package formatter;

import com.google.gson.JsonSyntaxException;
import formatter.factory.FormatterInitializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FormatterTest {
  private final String jsonOptions =
      """
            {
              "colonRules": {
                "before": true,
                "after": true
              },
              "equalSpaces": true,
              "printLineBreaks": 1
            }
            """;

  @Test
  public void varDeclarationTest() {
    String formattedCode =
        """
                let myVar : string = "Hello World!";
                """;
    MainFormatter formatter = FormatterInitializer.init(jsonOptions, formattedCode, "1.0");
    Assertions.assertEquals(formattedCode, formatter.formatProgram());
  }

  @Test
  public void assignationTest() {
    String formattedCode = """
                myVar = "Goodbye World!";
                """;
    MainFormatter formatter = FormatterInitializer.init(jsonOptions, formattedCode, "1.0");
    Assertions.assertEquals(formattedCode, formatter.formatProgram());
  }

  @Test
  public void varDeclarationWithExpressionTest() {
    String formattedCode = """
                let myVar : number = 2 + 2 * 5;
                """;
    MainFormatter formatter = FormatterInitializer.init(jsonOptions, formattedCode, "1.0");
    Assertions.assertEquals(formattedCode, formatter.formatProgram());
  }

  @Test
  public void printLnTest() {
    String formattedCode = """
                println("Hello World!");
                """;
    MainFormatter formatter = FormatterInitializer.init(jsonOptions, formattedCode, "1.0");
    Assertions.assertEquals(formattedCode, formatter.formatProgram());
  }

  @Test
  public void completeFormattingTest() {
    String formattedCode =
        """
                let myVar : number = 2 + 3 * 2;

                println(myVar);
                myVar = "Hello World!";

                println(myVar);
                """;
    MainFormatter formatter = FormatterInitializer.init(jsonOptions, formattedCode, "1.0");
    Assertions.assertEquals(formattedCode, formatter.formatProgram());
  }

  @Test
  public void differentFormatTest() {
    final String newJsonOptions =
        """
                    {
                    "colonRules": {
                      "before": true,
                      "after": false
                    },
                    "equalSpaces": false,
                    "printLineBreaks": 2
                  }
                """;
    String formattedCode =
        """
                let myVar :number=2 + 3 * 2;


                println(myVar);
                myVar="Hello World!";


                println(myVar);
                """;
    MainFormatter formatter = FormatterInitializer.init(newJsonOptions, formattedCode, "1.0");
    Assertions.assertEquals(formattedCode, formatter.formatProgram());
  }

  @Test
  public void invalidOptionsTest() {
    Assertions.assertThrows(
        JsonSyntaxException.class,
        () -> FormatterInitializer.init("invalid options json", "", "1.0"));
  }
}
