package interpreter.runtime;

import ast.Expression;
import ast.Statement;

public interface Evaluator {
    Expression evaluate(Expression statement);
}
