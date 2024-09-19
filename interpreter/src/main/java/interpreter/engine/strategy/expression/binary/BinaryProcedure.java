package interpreter.engine.strategy.expression.binary;

import ast.literal.Literal;

public interface BinaryProcedure {
  Literal<?> applyProcedure(Literal<?> left, Literal<?> right, String operator);

  boolean isApplicable(Literal<?> left, Literal<?> right, String operator);
}
