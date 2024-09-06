package interpreter.visitor;

import ast.visitor.NodeVisitor;
import interpreter.VariablesRepository;

public interface InterpreterVisitor extends NodeVisitor {
  VariablesRepository getVariablesRepository();
}
