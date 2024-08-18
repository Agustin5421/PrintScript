package ast.root;

import token.Position;
import visitors.ASTVisitor;

public interface ASTNode {
    ASTNodeType getType();
    Position start();
    Position end();
    ASTVisitor visit(ASTVisitor visitor);
}
