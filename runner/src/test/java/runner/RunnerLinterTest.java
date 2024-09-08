package runner;

import org.junit.jupiter.api.Test;

public class RunnerLinterTest {
  @Test
  public void runLinterTest() {
    String code = "let snake_case: string = \"Oliver\";\nlet snake_case: string = \"Oliver\";";

    Runner runner = new Runner();
    runner.analyze(code, "1.0", "{\n" +
        "  \"identifier\": {\n" +
        "    \"writingConvention\": {\n" +
        "      \"conventionName\": \"camelCase\",\n" +
        "      \"conventionPattern\": \"^[a-z]+(?:[A-Z]?[a-z0-9]+)*$\"\n" +
        "    }\n" +
        "  },\n" +
        "  \"callExpression\": {\n" +
        "    \"arguments\": [\"IDENTIFIER\", \"STRING_LITERAL\", \"NUMBER_LITERAL\"]\n" +
        "  }\n" +
        "}\n", new OutputResult() {
      @Override
      public OutputResult saveResult(String result) {
        System.out.println(result);
        return this;
      }

      @Override
      public String getResult() {
        return "";
      }
    });
  }
}
