package interpreter.visitor.strategy;

import ast.root.AstNode;
import ast.visitor.NodeVisitor;
import interpreter.visitor.InterpreterVisitorV3;

public interface InterpretingStrategy {
    NodeVisitor interpret(AstNode node, NodeVisitor visitor);
}
