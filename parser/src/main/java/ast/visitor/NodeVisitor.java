package ast.visitor;

import ast.root.AstNode;
import output.OutputResult;

public interface NodeVisitor {
  NodeVisitor visit(AstNode node);
}
