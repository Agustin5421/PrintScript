package interpreter.runtime;

import ast.ASTNode;
import ast.Expression;
import ast.Statement;

public interface Evaluator {
    ASTNode evaluate(ASTNode statement);
}
