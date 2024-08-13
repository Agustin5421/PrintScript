package ast;

import java.util.List;

public class CallExpression implements Expression{
    private final Identifier callee;
    private final List<Literal> arguments;

    public CallExpression(Identifier callee, List<Literal> arguments) {
        this.callee = callee;
        this.arguments = arguments;
    }

    public Identifier getCallee() {
        return callee;
    }

    public List<Literal> getArguments() {
        return arguments;
    }
}
