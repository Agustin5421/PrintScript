package formatter;

import com.google.gson.JsonSyntaxException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import runner.TestRunner;

public abstract class AbstractFormatterTest {
  protected abstract TestRunner setRunner(String jsonOptions, String formattedCode);

  @Test
  public void varDeclarationTest() {
    String formattedCode =
        """
                let myVar : string = "Hello World!";
                """;
    TestRunner testRunner = setRunner(getJsonOptions(), formattedCode);
    Assertions.assertEquals(formattedCode, testRunner.runFormatting());
  }

  @Test
  public void assignationTest() {
    String formattedCode =
        """
                let myVar : string = "Hello World!";
                myVar = "Goodbye World!";
                """;
    TestRunner testRunner = setRunner(getJsonOptions(), formattedCode);
    Assertions.assertEquals(formattedCode, testRunner.runFormatting());
  }

  @Test
  public void varDeclarationWithExpressionTest() {
    String formattedCode = """
                let myVar : number = 2 + 2 * 5;
                """;
    TestRunner testRunner = setRunner(getJsonOptions(), formattedCode);
    Assertions.assertEquals(formattedCode, testRunner.runFormatting());
  }

  @Test
  public void printLnTest() {
    String formattedCode = """
                println("Hello World!");
                """;
    TestRunner testRunner = setRunner(getJsonOptions(), formattedCode);
    Assertions.assertEquals(formattedCode, testRunner.runFormatting());
  }

  @Test
  public void completeFormattingTest() {
    String formattedCode =
        """
                let myVar : number = 2 + 3 * 2;

                println(myVar);
                myVar = 2;

                println(myVar);
                """;
    TestRunner testRunner = setRunner(getJsonOptions(), formattedCode);
    Assertions.assertEquals(formattedCode, testRunner.runFormatting());
  }

  @Test
  public void differentFormatTest() {
    String formattedCode =
        """
                let myVar: number=2 + 3 * 2;


                println(myVar);
                myVar=2;


                println(myVar);
                """;
    TestRunner testRunner = setRunner(alternativeOptions(), formattedCode);
    Assertions.assertEquals(formattedCode, testRunner.runFormatting());
  }

  @Test
  public void invalidOptionsTest() {
    Assertions.assertThrows(JsonSyntaxException.class, () -> setRunner("invalid options json", ""));
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
                "before": false,
                "after": true
              },
              "equalSpaces": false,
              "printLineBreaks": 2
            }
            """;
  }
}
