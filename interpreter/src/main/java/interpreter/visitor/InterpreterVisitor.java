package interpreter.visitor;

import ast.literal.Literal;
import ast.visitor.NodeVisitor;
import interpreter.visitor.repository.VariablesRepository;
import java.util.List;

public interface InterpreterVisitor extends NodeVisitor {
  VariablesRepository getVariablesRepository();

  InterpreterVisitor cloneVisitor();
}
