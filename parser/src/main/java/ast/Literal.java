package ast;

public interface Literal<T> extends Expression {
    T getValue();
}
