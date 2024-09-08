package cli;

import runner.Runner;
import java.io.FileInputStream;

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

    String code = findCode(codeFilePath);

    switch (operation) {
      case "Validation" -> runner.validate(code, version);
      case "Execution" -> runner.execute(code, version);
      case "Analyzing" -> runner.analyze(code,version, findCode(args[3]), new OutputString()); //args[3] is the options file
      case "Formatting" -> runner.format(code, version, findCode(args[3]));
      default -> throw new IllegalArgumentException("Unsupported operation: " + operation);
    }
  }

  private static String findCode(String codeFilePath) {
    try {
      return new FileInputStream(codeFilePath).toString();
    } catch (Exception e) {
      System.out.println("File not found");
      return null;
    }
  }
}
