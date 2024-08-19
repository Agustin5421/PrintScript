package interpreter.runtime;

import ast.root.AstNode;

public interface Evaluator {
  AstNode evaluate(AstNode statement);
}
