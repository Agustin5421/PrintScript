package ast.literal;

public class StringLiteral implements Literal<String> {
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
    public String getValue() {
        return value;
    }
}
