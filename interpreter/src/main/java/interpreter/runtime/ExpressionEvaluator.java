package interpreter.runtime;

import ast.*;
import interpreter.VariablesRepository;

public class ExpressionEvaluator implements Evaluator {
    VariablesRepository variablesRepository;

    public ExpressionEvaluator(VariablesRepository variablesRepository) {
        this.variablesRepository = variablesRepository;
    }

    // This should return a Literal at the end of the calling
    // (or else throw an exception if the variable is not defined)
    @Override
    public Expression evaluate(Expression statement) throws IllegalArgumentException {
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
            throw new IllegalArgumentException("Something else went wrong?");
        }
    }

    private Expression handleBinaryOperation(BinaryExpression statement) {
        Expression left = evaluate(statement.left());
        Expression right = evaluate(statement.right());
        return switch (statement.operator()) {
            case "+" -> validateAddOperation(left, right);
            case "-" ->
                    new NumberLiteral((getNumberDoubleValue(evaluate(left)) - getNumberDoubleValue(evaluate(right))));
            case "/" -> new NumberLiteral((getNumberDoubleValue(left) / getNumberDoubleValue(right)));
            case "*" -> new NumberLiteral((getNumberDoubleValue(left) * getNumberDoubleValue(right)));
            case "%" -> new NumberLiteral((getNumberDoubleValue(left) % getNumberDoubleValue(right)));
            default -> throw new IllegalArgumentException("Not a valid operation");
        };
    }

    private Expression validateAddOperation(Expression left, Expression right) {
        if (left instanceof StringLiteral || right instanceof StringLiteral) {
            return new StringLiteral((evaluate(left).toString() + evaluate(right).toString()));
        }
        else {
            return new NumberLiteral((getNumberDoubleValue(evaluate(left)) + getNumberDoubleValue(evaluate(right))));
        }
    }

    public double getNumberDoubleValue(Expression numberLiteral) {
        return ((NumberLiteral) numberLiteral).getValue().doubleValue();
    }
}
