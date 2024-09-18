package interpreter.visitor;

import ast.visitor.NodeVisitor;
import interpreter.visitor.repository.VariablesRepository;
import output.OutputResult;

public interface OutputVisitor extends NodeVisitor {
  public OutputVisitor setVariablesRepository(VariablesRepository repository);

  public VariablesRepository getVariablesRepository();

  OutputResult<String> getOutputResult();
}
