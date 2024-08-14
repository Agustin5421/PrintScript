package ast;

import ast.records.ASTNodeType;
import token.Position;

public interface ASTNode {
    ASTNodeType getType();
    Position start();
    Position end();
}
