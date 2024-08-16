package interpreter.runtime;

import ast.root.ASTNode;

public interface Evaluator {
    ASTNode evaluate(ASTNode statement);
}
