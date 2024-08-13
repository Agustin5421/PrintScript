package interpreter.runtime;

import ast.Expression;
import ast.Statement;

public interface Evaluator {
    void evaluate(Expression statement);
}
