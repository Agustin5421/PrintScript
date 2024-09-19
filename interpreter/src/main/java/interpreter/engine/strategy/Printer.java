package interpreter.engine.strategy;

import ast.literal.Literal;
import output.OutputResult;

public class Printer {
  public void apply(Literal<?> literal, OutputResult<String> outputResult) {
    outputResult.saveResult(literal.value().toString());
  }
}
