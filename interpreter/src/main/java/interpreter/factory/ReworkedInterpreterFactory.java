package interpreter.factory;

import factory.ParserFactory;
import interpreter.ReworkedInterpreter;
import interpreter.visitor.OutputVisitor;
import interpreter.visitor.factory.InterpreterVisitorV3Factory;
import output.OutputResult;
import parsers.Parser;

public class ReworkedInterpreterFactory {
  public static ReworkedInterpreter buildInterpreter(
      String version, OutputResult<String> outputResult) {
    OutputVisitor visitor = InterpreterVisitorV3Factory.getVisitor(version, outputResult);
    Parser parser = ParserFactory.getParser(version);
    return new ReworkedInterpreter(parser, visitor);
  }
}
