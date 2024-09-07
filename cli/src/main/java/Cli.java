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

    switch (operation) {
      case "Validation" -> runner.validate(codeFilePath, version);
      case "Analyzing" -> runner.analyze(codeFilePath,version, args[3]); //args[3] is the options file
      case "Formatting" -> runner.format(codeFilePath, version, args[3]);
      case "Execution" -> runner.execute(codeFilePath, version);
      default -> throw new IllegalArgumentException("Unsupported operation: " + operation);
    }
  }
}
