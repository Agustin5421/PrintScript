package formatter;

import com.google.gson.JsonSyntaxException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public abstract class AbstractFormatterTest {
  protected abstract MainFormatter initFormatter(String jsonOptions, String formattedCode);

  @Test
  public void varDeclarationTest() {
    String formattedCode =
        """
                let myVar : string = "Hello World!";
                """;
    MainFormatter formatter = initFormatter(getJsonOptions(), formattedCode);
    Assertions.assertEquals(formattedCode, formatter.formatProgram());
  }

  @Test
  public void assignationTest() {
    String formattedCode = """
                myVar = "Goodbye World!";
                """;
    MainFormatter formatter = initFormatter(getJsonOptions(), formattedCode);
    Assertions.assertEquals(formattedCode, formatter.formatProgram());
  }

  @Test
  public void varDeclarationWithExpressionTest() {
    String formattedCode = """
                let myVar : number = 2 + 2 * 5;
                """;
    MainFormatter formatter = initFormatter(getJsonOptions(), formattedCode);
    Assertions.assertEquals(formattedCode, formatter.formatProgram());
  }

  @Test
  public void printLnTest() {
    String formattedCode = """
                println("Hello World!");
                """;
    MainFormatter formatter = initFormatter(getJsonOptions(), formattedCode);
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
    MainFormatter formatter = initFormatter(getJsonOptions(), formattedCode);
    Assertions.assertEquals(formattedCode, formatter.formatProgram());
  }

  @Test
  public void differentFormatTest() {
    String formattedCode =
        """
                let myVar :number=2 + 3 * 2;


                println(myVar);
                myVar="Hello World!";


                println(myVar);
                """;
    MainFormatter formatter = initFormatter(alternativeOptions(), formattedCode);
    Assertions.assertEquals(formattedCode, formatter.formatProgram());
  }

  @Test
  public void invalidOptionsTest() {
    Assertions.assertThrows(
        JsonSyntaxException.class, () -> initFormatter("invalid options json", ""));
  }

  protected String getJsonOptions() {
    return """
            {
              "colonRules": {
                "before": true,
                "after": true
              },
              "equalSpaces": true,
              "printLineBreaks": 1
            }
            """;
  }

  protected String alternativeOptions() {
    return """
            {
              "colonRules": {
                "before": true,
                "after": false
              },
              "equalSpaces": false,
              "printLineBreaks": 2
            }
            """;
  }
}
