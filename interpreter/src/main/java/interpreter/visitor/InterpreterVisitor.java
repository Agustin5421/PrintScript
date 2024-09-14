package interpreter.visitor;

import ast.literal.Literal;
import ast.literal.NumberLiteral;
import ast.visitor.NodeVisitor;
import interpreter.visitor.repository.VariablesRepository;

public interface InterpreterVisitor extends NodeVisitor {
  VariablesRepository getVariablesRepository();

  InterpreterVisitor cloneVisitor();

  default Literal<?> getValue() {
    return new NumberLiteral(0, null, null);
  }
}
