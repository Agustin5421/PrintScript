package ast.visitor;

import ast.root.AstNode;

public interface NodeVisitor {
  NodeVisitor visit(AstNode node);
}
