package interpreter.visitor;

import interpreter.visitor.repository.VariablesRepository;

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

  public static InterpreterVisitor getInterpreterVisitor(VariablesRepository variablesRepository) {
    String version = VersionManager.getCurrentVersion();
    return switch (version) {
      case "1.0" -> new InterpreterVisitorV1(variablesRepository);
      case "1.1" -> new InterpreterVisitorV2(
          new InterpreterVisitorV1(variablesRepository), variablesRepository);
      default -> throw new IllegalArgumentException("Invalid version: " + version);
    };
  }
}
