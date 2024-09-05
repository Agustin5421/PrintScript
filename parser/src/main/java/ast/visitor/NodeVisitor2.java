package ast.visitor;

import ast.root.AstNode;

public interface NodeVisitor2 {
    NodeVisitor visit(AstNode node);
}
