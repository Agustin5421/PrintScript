package ast.root;

import ast.visitor.NodeVisitor;

public interface VisitableNode {
    NodeVisitor accept(NodeVisitor visitor);
}
