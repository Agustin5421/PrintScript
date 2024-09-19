package ast.root;

import token.Position;

public interface AstNode extends VisitableNode {
  AstNodeType getNodeType();

  Position start();

  Position end();
}
