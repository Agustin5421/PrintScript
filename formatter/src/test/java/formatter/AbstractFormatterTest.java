package formatter;

import com.google.gson.JsonSyntaxException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
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
    Assertions.assertEquals(formattedCode.trim(), testRunner.runFormatting().trim());
  }

  @Test
  public void assignationTest() {
    String formattedCode =
        """
                let myVar : string = "Hello World!";
                myVar = "Goodbye World!";
                """;
    TestRunner testRunner = setRunner(getJsonOptions(), formattedCode);
    Assertions.assertEquals(formattedCode.trim(), testRunner.runFormatting().trim());
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
        readFileContent("src/test/resources/versions/common/complete-formatting.ps");
    TestRunner testRunner = setRunner(getJsonOptions(), formattedCode);
    Result result = getResult(formattedCode, testRunner.runFormatting());
    Assertions.assertEquals(result.expected, result.json);
  }

  @Test
  public void differentFormatTest() {
    String formattedCode =
        readFileContent("src/test/resources/versions/common/different-format.ps");
    TestRunner testRunner = setRunner(alternativeOptions(), formattedCode);
    Result result = getResult(formattedCode, testRunner.runFormatting());
    Assertions.assertEquals(result.expected, result.json);
  }

  @Test
  public void invalidOptionsTest() {
    Assertions.assertThrows(JsonSyntaxException.class, () -> setRunner("invalid options json", ""));
  }

  protected String getJsonOptions() {
    return readFileContent("src/test/resources/versions/common/options.json");
  }

  protected String alternativeOptions() {
    return readFileContent("src/test/resources/versions/common/altOptions.json");
  }

  protected String readFileContent(String filePath) {
    try {
      return Files.readString(Paths.get(filePath));
    } catch (NoSuchFileException e) {
      System.err.println("File not found: " + filePath);
      return null;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  protected static Result getResult(String json, String expected) {
    json = json.replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n").replaceAll("    ", "\t");
    expected = expected.replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n").replaceAll("    ", "\t");
    return new Result(json, expected);
  }

  protected record Result(String json, String expected) {}
}
