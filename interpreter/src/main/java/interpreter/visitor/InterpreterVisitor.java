package interpreter.visitor;

import ast.visitor.NodeVisitor;
import interpreter.visitor.repository.VariablesRepository;

public interface InterpreterVisitor extends NodeVisitor {
  public InterpreterVisitor setVariablesRepository(VariablesRepository repository);

  public VariablesRepository getVariablesRepository();
}
