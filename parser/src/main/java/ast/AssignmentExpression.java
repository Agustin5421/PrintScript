package ast;

import token.Position;

public class AssignmentExpression implements Expression{
    private final Identifier left;
    private final Identifier right;
    private final String operator;
    private final Position start;
    private final Position end;

    public AssignmentExpression(Identifier left, Identifier right, String operator, Position start, Position end) {
        this.left = left;
        this.right = right;
        this.operator = operator;
        this.start = start;
        this.end = end;
    }

    public Identifier getLeft() {
        return left;
    }

    public Identifier getRight() {
        return right;
    }

    public String getOperator() {
        return operator;
    }

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }


}
