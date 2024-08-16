package ast.literal;

import ast.expressions.Expression;

public interface Literal<T> extends Expression {
    T value();
}
