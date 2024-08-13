package ast;

public class AssignmentExpression implements Expression{
    private final Identifier left;
    private final Identifier right;
    private final String operator;

    public AssignmentExpression(Identifier left, Identifier right, String operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
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

}
