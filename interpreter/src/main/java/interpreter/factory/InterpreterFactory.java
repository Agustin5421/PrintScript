package interpreter.factory;

import factory.ParserFactory;
import interpreter.Interpreter;
import interpreter.visitor.InterpreterVisitor;
import interpreter.visitor.InterpreterVisitorFactory;
import parsers.Parser;

public class InterpreterFactory {
  public static Interpreter getInterpreter(String version) {
    return switch (version) {
      case "1.0" -> getInterpreterV1();
      case "1.1" -> getInterpreterV2();
      default -> throw new IllegalArgumentException("Invalid version: " + version);
    };
  }

  private static Interpreter getInterpreterV1() {
    Parser parser = ParserFactory.getParser("1.0");
    InterpreterVisitor interpreterVisitor = InterpreterVisitorFactory.getInterpreterVisitor("1.0");
    return new Interpreter(parser, interpreterVisitor);
  }

  private static Interpreter getInterpreterV2() {
    Parser parser = ParserFactory.getParser("1.1");
    InterpreterVisitor interpreterVisitor = InterpreterVisitorFactory.getInterpreterVisitor("1.1");
    return new Interpreter(parser, interpreterVisitor);
  }
}
