package interpreter.visitor;

import ast.visitor.NodeVisitor;
import output.OutputResult;

public interface OutputVisitor extends NodeVisitor {
  OutputResult<String> getOutputResult();
}
