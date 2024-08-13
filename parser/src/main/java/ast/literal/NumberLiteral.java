package ast.literal;

public class NumberLiteral implements Literal<Number> {
    private final Number value;
    public NumberLiteral(Number value) {
        this.value = value;
    }

    public Number getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "LiteralNumber{" +
                "value=" + value +
                '}';
    }
}
