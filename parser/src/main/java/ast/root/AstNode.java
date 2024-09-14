package ast.root;

import ast.visitor.NodeVisitor;
import token.Position;

public interface AstNode extends VisitableNode {
  AstNodeType getNodeType();

  Position start();

  Position end();
}
