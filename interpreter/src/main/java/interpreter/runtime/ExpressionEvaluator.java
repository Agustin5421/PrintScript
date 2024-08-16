// interpreter/src/main/java/interpreter/runtime/ExpressionEvaluator.java

package interpreter.runtime;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.Literal;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.root.ASTNode;
import interpreter.VariablesRepository;
import token.Position;

public class ExpressionEvaluator implements Evaluator {
    private final VariablesRepository variablesRepository;
    private final int line;
    private int column;
    // TODO handle the position of each literal created
    private final Position defaultPosition;

    public ExpressionEvaluator(VariablesRepository variablesRepository, int line) {
        this.variablesRepository = variablesRepository;
        this.line = line;
        this.defaultPosition = new Position(line, 0);
    }

    // This should return a Literal at the end of the calling
    // (or else throw an exception if the variable is not defined)
    @Override
    public ASTNode evaluate(ASTNode statement) throws IllegalArgumentException {
        try {
            if (statement instanceof BinaryExpression) {
                return handleBinaryOperation((BinaryExpression) statement);
            } else if (statement instanceof Literal<?>) {
                return statement;
            } else if (statement instanceof Identifier) {
                try {
                    Object value = variablesRepository.getVariable(((Identifier) statement).name());
                    if (value instanceof String) {
                        return new StringLiteral((String) value, defaultPosition, defaultPosition);
                    } else {
                        return new NumberLiteral((Number) value, defaultPosition, defaultPosition);
                    }
                } catch (IllegalArgumentException exception) {
                    throw new IllegalArgumentException("Trying to perform an invalid arithmetic operation at: " + line + "col: " + column);
                }
            } else {
                throw new IllegalArgumentException("Something else went wrong?");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Trying to perform an invalid arithmetic operation at: " + line + "col: " + column);
        }
    }

    private ASTNode handleBinaryOperation(BinaryExpression statement) {
        ASTNode left = evaluate(statement.left());
        ASTNode right = evaluate(statement.right());
        return switch (statement.operator()) {
            case "+" -> validateAddOperation(left, right);
            case "-" -> validateOperation(left, right, (a, b) -> checkIfBothIntegers(a, b) ? a.intValue() - b.intValue() : a.doubleValue() - b.doubleValue());
            case "/" -> validateOperation(left, right, (a, b) -> checkIfBothIntegers(a, b) ? a.intValue() / b.intValue() : a.doubleValue() / b.doubleValue());
            case "*" -> validateOperation(left, right, (a, b) -> checkIfBothIntegers(a, b) ? a.intValue() * b.intValue() : a.doubleValue() * b.doubleValue());
            case "%" -> validateOperation(left, right, (a, b) -> checkIfBothIntegers(a, b) ? a.intValue() % b.intValue() : a.doubleValue() % b.doubleValue());
            default -> throw new IllegalArgumentException("Not a valid operation");
        };
    }

    private boolean checkIfBothIntegers(Number a, Number b) {
        return a instanceof Integer && b instanceof Integer;
    }

    private ASTNode validateAddOperation(ASTNode left, ASTNode right) {
        if (left instanceof StringLiteral || right instanceof StringLiteral) {
            return new StringLiteral(((Literal<?>) evaluate(left)).value().toString() +
                    ((Literal<?>) evaluate(right)).value().toString(), defaultPosition, defaultPosition);
        } else {
            return validateOperation(left, right, (a, b) ->
                    checkIfBothIntegers(a, b) ? a.intValue() + b.intValue() : a.doubleValue() + b.doubleValue());
        }
    }

    private ASTNode validateOperation(ASTNode left, ASTNode right, NumberBinaryOperator operator) {
        Number leftValue = getNumberValue(left);
        Number rightValue = getNumberValue(right);
        return createNumberLiteral(applyOperation(leftValue, rightValue, operator), left, right);
    }

    private Number applyOperation(Number left, Number right, NumberBinaryOperator operator) {
        if (checkIfBothIntegers(left, right)) {
            return operator.apply(left.intValue(), right.intValue());
        } else {
            return operator.apply(left.doubleValue(), right.doubleValue());
        }
    }

    private Number getNumberValue(ASTNode expression) {
        if (expression instanceof NumberLiteral numberLiteral) {
            return numberLiteral.value();
        } else {
            throw new IllegalArgumentException("Expression is not a NumberLiteral");
        }
    }

    private NumberLiteral createNumberLiteral(Number result, ASTNode left, ASTNode right) {
        if (left instanceof NumberLiteral leftLiteral && right instanceof NumberLiteral rightLiteral) {
            if (checkIfBothIntegers(leftLiteral.value(), rightLiteral.value())) {
                return new NumberLiteral(result.intValue(), defaultPosition, defaultPosition);
            } else {
                return new NumberLiteral(result.doubleValue(), defaultPosition, defaultPosition);
            }
        } else {
            throw new IllegalArgumentException("Both expressions must be NumberLiterals");
        }
    }

    @FunctionalInterface
    interface NumberBinaryOperator {
        Number apply(Number left, Number right);
    }
}