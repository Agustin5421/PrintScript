package ast.root;

import token.Position;

public interface AstNode {
  AstNodeType getType();

  Position start();

  Position end();
}
