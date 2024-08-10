package ast;

public class StringLiteral implements Literal {
    private final String value;
    public StringLiteral(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "LiteralString{" +
                "value='" + value + '\'' +
                '}';
    }

    @Override
    public Object getValue() {
        return value;
    }
}
