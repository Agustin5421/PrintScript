package interpreter.runtime;

import ast.BinaryExpression;
import ast.Expression;
import ast.Literal;

public class BinaryExpressionEvaluator implements Evaluator {
    @Override
    public void evaluate(Expression statement) {
        BinaryExpression binaryExpression = (BinaryExpression) statement;
        Expression left = binaryExpression.left;
        Expression right = binaryExpression.right;
        String operator = binaryExpression.operator;
        if (checkLiterals(left, right)) {
            Literal<?> left1 = (Literal<?>) left;
            Literal<?> right1 = (Literal<?>) right;
            if (checkStrings(left1, right1)) {
                evaluateStringOperations((Literal<String>) left1, (Literal<String>) right1, operator);
            }
            else if (checkNumbers(left1, right1)){
                evaluateNumberOperations((Literal<Integer>) left1, (Literal<Integer>) right1, operator);
            }
            else {
                // TODO implement number and string operations?
                throw new RuntimeException("Not implemented number and string operations yet");
            }
        }
        else {

        }
        // TODO implement non literal operations
        throw new RuntimeException("Not implemented variable operations yet");
    }

    private void evaluateBinaryExpression() {

    }

    private void evaluateNumberOperations(Literal<Integer> left, Literal<Integer> right, String operator) {
        Integer result;
        switch (operator) {
            case "+":
                result = left.getValue() + right.getValue();
                break;
            case "-":
                result = left.getValue() - right.getValue();
                break;
            case "*":
                result = left.getValue() * right.getValue();
                break;
            case "/":
                result = left.getValue() / right.getValue();
                break;
            case "%":
                result = left.getValue() % right.getValue();
                break;
        }
    }

    private void evaluateStringOperations(Literal<String> left, Literal<String> right, String operator) {
        String result;
        switch (operator) {
            case "+":
                result = left.getValue() + right.getValue();
                break;
            default:
                throw new RuntimeException("Not an opperation supported for strings");
        }
    }

    private static boolean checkLiterals(Expression left, Expression right) {
        return left instanceof Literal && right instanceof Literal;
    }

    private boolean checkNumbers(Literal<?> left, Literal<?> right) {
        return left.getValue() instanceof Integer && right.getValue() instanceof Integer;
    }

    private static boolean checkStrings(Literal<?> left, Literal<?> right) {
        return left.getValue() instanceof String && right.getValue() instanceof String;
    }
}
