package formatter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import runner.TestRunner;

public class FormatterV2Test extends AbstractFormatterTest {
  @Override
  protected TestRunner setRunner(String jsonOptions, String formattedCode) {
    return new TestRunner(jsonOptions, formattedCode, "1.1");
  }

  @Override
  public String getJsonOptions() {
    return readFileContent("src/test/resources/versions/1.1/options.json");
  }

  @Override
  public String alternativeOptions() {
    return readFileContent("src/test/resources/versions/1.1/altOptions.json");
  }

  @Test
  public void testBooleanDeclaration() {
    String formattedCode = """
                let myVar : boolean = true;
                """;
    TestRunner testRunner = setRunner(getJsonOptions(), formattedCode);
    Assertions.assertEquals(formattedCode, testRunner.runFormatting());
  }

  @Test
  public void testBooleanReAssignation() {
    String formattedCode =
        """
                let anotherVar : boolean = true;
                anotherVar = false;
                """;
    TestRunner testRunner = setRunner(getJsonOptions(), formattedCode);
    Assertions.assertEquals(formattedCode, testRunner.runFormatting());
  }

  @Test
  public void testReadInput() {
    String formattedCode = """
            let myVar : string = readInput();
            """;
    TestRunner testRunner = setRunner(getJsonOptions(), formattedCode);
    Assertions.assertEquals(formattedCode, testRunner.runFormatting());
  }

  @Test
  public void testReadEnv() {
    String formattedCode = """
            let myVar : string = readEnv("ENV_VAR");
            """;
    TestRunner testRunner = setRunner(getJsonOptions(), formattedCode);
    Assertions.assertEquals(formattedCode, testRunner.runFormatting());
  }

  @Test
  public void newCompleteFormattingTest() {
    String code = readFileContent("src/test/resources/versions/1.1/complete-formatting/input.ps");
    String formattedCode =
        readFileContent("src/test/resources/versions/1.1/complete-formatting/output.ps");
    TestRunner testRunner = setRunner(getJsonOptions(), code);
    Result result = getResult(formattedCode, testRunner.runFormatting());
    Assertions.assertEquals(result.expected(), result.json());
  }

  @Test
  public void newDifferentFormatTest() {
    String code = readFileContent("src/test/resources/versions/1.1/different-format/input.ps");
    String formattedCode =
        readFileContent("src/test/resources/versions/1.1/different-format/output.ps");
    TestRunner testRunner = setRunner(alternativeOptions(), code);
    Result result = getResult(formattedCode, testRunner.runFormatting());
    Assertions.assertEquals(result.expected(), result.json());
  }
}
