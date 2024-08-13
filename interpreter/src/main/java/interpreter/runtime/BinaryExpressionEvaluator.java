package interpreter.runtime;

import ast.*;
import interpreter.VariablesRepository;

public class BinaryExpressionEvaluator implements Evaluator {
    VariablesRepository variablesRepository;
    public BinaryExpressionEvaluator(VariablesRepository variablesRepository) {
        this.variablesRepository = variablesRepository;
    }

    @Override
    public Expression evaluate(Expression statement) {
        if (statement instanceof BinaryExpression) {
            return handleBinaryOperation((BinaryExpression) statement);
        }
        else if (statement instanceof Literal<?>) {
            return statement;
        }
        else if (statement instanceof Identifier) {
            try {
                String variable = variablesRepository.getStringVariable(((Identifier) statement).getName());
                return new StringLiteral(variable);
            }
            catch (IllegalArgumentException exception) {
                Number variable = variablesRepository.getNumberVariable(((Identifier) statement).getName());
                return new NumberLiteral(variable);
            }
        }
        else {
            throw new RuntimeException("Something else went wrong?");
        }
    }

    private Expression handleBinaryOperation(BinaryExpression statement) {
        Expression left = evaluate(statement.left);
        Expression right = evaluate(statement.right);
        if (statement.operator.equals("+")) {
            return validateAddOperation(left, right);
        }
        else {
            switch (statement.operator) {
                case "-":
                    return new NumberLiteral((getNumberDoubleValue(evaluate(left)) - getNumberDoubleValue(evaluate(right))));
                case "/":
                    return new NumberLiteral((getNumberDoubleValue(left) / getNumberDoubleValue(right)));
                case "*":
                    return new NumberLiteral((getNumberDoubleValue(left) * getNumberDoubleValue(right)));
                case "%":
                    return new NumberLiteral((getNumberDoubleValue(left) % getNumberDoubleValue(right)));
                default:
                    throw new RuntimeException("Not a valid operation");
            }
        }
    }

    private Expression validateAddOperation(Expression left, Expression right) {
        if (left instanceof StringLiteral || right instanceof StringLiteral) {
            String leftValue = left instanceof StringLiteral ? ((StringLiteral) left).getValue() : String.valueOf(((Literal<?>) left).getValue());
            String rightValue = right instanceof StringLiteral ? ((StringLiteral) right).getValue() : String.valueOf(((Literal<?>) right).getValue());
            return new StringLiteral(leftValue + rightValue);
        }
        else if (left instanceof NumberLiteral && right instanceof NumberLiteral) {
            return new NumberLiteral(((NumberLiteral) left).getValue().doubleValue() + ((NumberLiteral) right).getValue().doubleValue());
        }
        else {
            // TODO check for variable operations
            return null;
        }
    }

    public double getNumberDoubleValue(Expression numberLiteral) {
        return ((NumberLiteral) numberLiteral).getValue().doubleValue();
    }
}
