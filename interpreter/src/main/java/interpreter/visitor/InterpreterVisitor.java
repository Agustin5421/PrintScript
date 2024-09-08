package interpreter.visitor;

import ast.literal.Literal;
import ast.visitor.NodeVisitor;
import interpreter.VariablesRepository;
import java.util.List;

public interface InterpreterVisitor extends NodeVisitor {
  VariablesRepository getVariablesRepository();

  List<String> getPrintedValues();

  InterpreterVisitor getPreviousVisitor();

  Literal<?> getValue();
}
