package runner;

import java.io.ByteArrayInputStream;
import org.junit.jupiter.api.Test;
import output.OutputResult;
import report.Report;

public class RunnerLinterTest {
  @Test
  public void runLinterTest() {
    String code = "let snake_case: string = \"Oliver\";\nlet camel_case: string = \"Oliver\";";

    Runner runner = new Runner();
    runner.analyze(
        new ByteArrayInputStream(code.getBytes()),
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
        new OutputResult<>() {
          @Override
          public void saveResult(Report result) {
            System.out.println(result.toString());
          }

          @Override
          public Report getResult() {
            return null;
          }
        });
  }
}
