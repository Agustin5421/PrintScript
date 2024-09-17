package runner;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import output.OutputResult;

public class RunnerLinterTest {
  @Test
  public void runLinterTest() {
    String code = "let snake_case: string = \"Oliver\";\nlet camel_case: string = \"Oliver\";";

    Runner runner = new Runner();
    runner.analyze(
            new ByteArrayInputStream(code.getBytes()).toString(),
        "1.0",
            """
                    {
                      "identifier": {
                        "writingConvention": {
                          "conventionName": "camelCase",
                          "conventionPattern": "^[a-z]+(?:[A-Z]?[a-z0-9]+)*$"
                        }
                      },
                      "callExpression": {
                        "arguments": ["IDENTIFIER", "STRING_LITERAL", "NUMBER_LITERAL"]
                      }
                    }
                    """,
        new OutputResult<String>() {
          @Override
          public void saveResult(String result) {
            System.out.println(result);
          }

          @Override
          public String getResult() {
            return "";
          }
        });
  }
}
