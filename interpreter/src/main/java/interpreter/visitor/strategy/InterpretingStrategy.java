package interpreter.visitor.strategy;

import ast.visitor.NodeVisitor;
import strategy.Strategy;

public interface InterpretingStrategy extends Strategy<NodeVisitor> {}
