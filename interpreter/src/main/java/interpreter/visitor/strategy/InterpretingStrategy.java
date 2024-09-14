package interpreter.visitor.strategy;

import ast.root.AstNode;
import interpreter.visitor.InterpreterVisitorV3;

public interface InterpretingStrategy {
    InterpreterVisitorV3 interpret(AstNode node, InterpreterVisitorV3 visitor);
}
