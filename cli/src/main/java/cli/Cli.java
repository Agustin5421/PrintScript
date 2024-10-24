package cli;

import input.InputSystem;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import observers.ProgressObserver;
import observers.ProgressPrinter;
import output.OutputMock;
import output.OutputReportSystem;
import output.OutputStringSystem;
import runner.Runner;

public class Cli {
  // .\gradlew :Cli:run --args="Validation cli/src/main/resources/clitest.txt"
  // .\gradlew :cli:run --args="Execution cli/src/main/resources/clitest.txt"
  // .\gradlew :cli:run --args="Formatter cli/src/main/resources/clitest.txt
  // src/main/resources/formatterOptionsTest.json"
  // .\gradlew :cli:run --args="Analyzing cli/src/main/resources/clitest.txt
  // src/main/resources/linterOptionsTest.json"

  public static void main(String[] args) throws IOException {
    /*
    if (args.length < 3) {
      System.out.println("Please enter a valid instruction");
      return;
    }
     */

    ProgressPrinter progressPrinter = new ProgressPrinter();
    ProgressObserver progressObserver = new ProgressObserver(progressPrinter);

    Runner runner = new Runner(progressObserver);
    //    Runner runner = new Runner();

    String operation = "Analyzing"; // args[0];
    String codeFilePath = "cli/src/main/resources/clitest.txt"; // args[1];
    String version = "1.1"; // args[2];
    String configPath = "cli/src/main/resources/linterOptionsTest.json"; // args[3];
    String config = readJsonFile(configPath);

    //    String operation = args[0];
    //    String codeFilePath = args[1];
    //    String version = args[2];

    InputStream code = findCode(codeFilePath);

    // args[3] is the options file
    try {
      switch (operation) {
        case "Validation" -> runner.validate(code, version);
        case "Execution" -> runner.execute(
            code, version, new OutputStringSystem(), new OutputStringSystem(), new InputSystem());
        case "Analyzing" -> runner.analyze(code, version, config, new OutputReportSystem());
        case "Formatting" -> runner.format(
            code, version, findCode(args[3]).toString(), new OutputMock());
        default -> {
          progressObserver.error();
          throw new IllegalArgumentException("Unsupported operation: " + operation);
        }
      }
    } catch (Exception e) {
      progressObserver.error();
      System.out
          .println(); // Empty line since the progress bar would be overwritten by the error message
      System.out.println("Error: " + e.getMessage());
    }
  }

  private static InputStream findCode(String codeFilePath) {
    try {
      return new FileInputStream(codeFilePath);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String readJsonFile(String filePath) throws IOException {
    return new String(Files.readAllBytes(Paths.get(filePath)));
  }
}
