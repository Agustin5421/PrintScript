package cli;

import java.io.FileInputStream;
import java.io.InputStream;

import output.OutputMock;
import output.OutputString;
import runner.Runner;

public class Cli {
  private static final Runner runner = new Runner();

  // .\gradlew :cli:run --args="Validation src/main/resources/clitest.txt"
  // .\gradlew :cli:run --args="Execution src/main/resources/clitest.txt"
  // .\gradlew :cli:run --args="Formatter src/main/resources/clitest.txt
  // src/main/resources/formatterOptionsTest.json"
  // .\gradlew :cli:run --args="Analyzing src/main/resources/clitest.txt
  // src/main/resources/linterOptionsTest.json"

  public static void main(String[] args) {
    if (args.length < 3) {
      System.out.println("Please enter a valid instruction");
      return;
    }

    String operation = args[0];
    String codeFilePath = args[1];
    String version = args[2];

    InputStream code = findCode(codeFilePath);

    //args[3] is the options file
    switch (operation) {
      case "Validation" -> runner.validate(code, version);
      case "Execution" -> runner.execute(code, version, new OutputMock(), new OutputMock());
      case "Analyzing" -> runner.analyze(code, version, findCode(args[3]).toString(), new OutputString());
      case "Formatting" -> runner.format(code, version, findCode(args[3]).toString());
      default -> throw new IllegalArgumentException("Unsupported operation: " + operation);
    }
  }

  private static InputStream findCode(String codeFilePath) {
    try {
      return new FileInputStream(codeFilePath);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
  }
}
