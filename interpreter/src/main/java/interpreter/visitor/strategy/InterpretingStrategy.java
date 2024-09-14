package interpreter.visitor.strategy;

import ast.root.AstNode;
import ast.visitor.NodeVisitor;

public interface InterpretingStrategy {
  NodeVisitor interpret(AstNode node, NodeVisitor visitor);
}
