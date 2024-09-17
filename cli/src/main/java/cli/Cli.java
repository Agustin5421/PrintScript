package cli;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import observers.ProgressObserver;
import observers.ProgressPrinter;
import output.OutputMock;
import output.OutputString;
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
    ProgressObserver progressObserver = new ProgressObserver(progressPrinter, 3);

//    Runner runner = new Runner(progressObserver);
    Runner runner = new Runner();

    String operation = "Validation";                                //args[0];
    String codeFilePath = "cli/src/main/resources/clitest.txt";     //args[1];
    String version = "1.1";                                         //args[2];

//    String operation = args[0];
//    String codeFilePath = args[1];
//    String version = args[2];

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
