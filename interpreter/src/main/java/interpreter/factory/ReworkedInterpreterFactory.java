package interpreter.factory;

import interpreter.ReworkedInterpreter;
import interpreter.visitor.OutputVisitor;
import interpreter.visitor.factory.InterpreterVisitorV3Factory;
import output.OutputResult;

public class ReworkedInterpreterFactory {
  public static ReworkedInterpreter buildInterpreter(
      String version, OutputResult<String> outputResult) {
    OutputVisitor visitor = InterpreterVisitorV3Factory.getVisitor(version, outputResult);

    return new ReworkedInterpreter(visitor);
  }
}
