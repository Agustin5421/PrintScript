package interpreter.visitor;

import interpreter.visitor.repository.VariablesRepository;
import java.util.List;

public class InterpreterVisitorFactory {
  public static InterpreterVisitor getInterpreterVisitor(String version) {
    VariablesRepository variablesRepository = new VariablesRepository();
    return switch (version) {
      case "1.0" -> new InterpreterVisitorV1(variablesRepository);
      case "1.1" -> new InterpreterVisitorV2(
          new InterpreterVisitorV1(variablesRepository), variablesRepository);
      default -> throw new IllegalArgumentException("Invalid version: " + version);
    };
  }

  public static InterpreterVisitor getInterpreterVisitorWithParams(
      VariablesRepository variablesRepository, List<String> printedValues) {
    String version = VersionManager.getCurrentVersion();
    return switch (version) {
      case "1.0" -> new InterpreterVisitorV1(variablesRepository, printedValues);
      case "1.1" -> new InterpreterVisitorV2(
          new InterpreterVisitorV1(variablesRepository), variablesRepository, printedValues);
      default -> throw new IllegalArgumentException("Invalid version: " + version);
    };
  }
}
