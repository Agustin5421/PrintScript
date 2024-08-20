package ast.root;

import ast.visitor.NodeVisitor;
import token.Position;

public interface AstNode {
  AstNodeType getType();

  Position start();

  Position end();

  void accept(NodeVisitor visitor);
}
