package ast;

import token.Position;

import java.util.List;

public class CallExpression implements Expression{
    private final Identifier callee;
    private final List<Literal> arguments;
    private final Position start;
    private final Position end;

    public CallExpression(Identifier callee, List<Literal> arguments, Position start, Position end) {
        this.callee = callee;
        this.arguments = arguments;
        this.start = start;
        this.end = end;
    }

    public Identifier getCallee() {
        return callee;
    }

    public List<Literal> getArguments() {
        return arguments;
    }

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }

}
