package ast.root;

import visitor.NodeVisitor;

public interface VisitableNode {
  NodeVisitor accept(NodeVisitor visitor);
}
