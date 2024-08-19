package ast.root;

import token.Position;

public interface ASTNode {
  ASTNodeType getType();

  Position start();

  Position end();
}
