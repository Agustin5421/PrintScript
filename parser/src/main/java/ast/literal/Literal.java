package ast.literal;

import ast.Expression;

public interface Literal<T> extends Expression {
    T value();
}
