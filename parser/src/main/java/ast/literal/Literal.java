package ast.literal;

import ast.expressions.ExpressionNode;

public interface Literal<T> extends ExpressionNode {
  T value();
}
