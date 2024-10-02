package ast.root;

import position.Position;

public interface AstNode extends VisitableNode {
  AstNodeType getNodeType();

  Position start();

  Position end();
}
