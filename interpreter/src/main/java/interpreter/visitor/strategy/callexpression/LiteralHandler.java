package interpreter.visitor.strategy.callexpression;

import ast.literal.Literal;
import ast.statements.CallExpression;
import interpreter.visitor.strategy.literal.LiteralFactory;
import token.Position;

import java.util.Map;

public class LiteralHandler{
    private final Map<String, LiteralFactory> strategies;

    public LiteralHandler(Map<String, LiteralFactory> strategies) {
        this.strategies = strategies;
    }

    public Literal<?> getLiteral(CallExpression callExpression, String userInput) {
        Position start = callExpression.start();
        Position end = callExpression.end();

        for (Map.Entry<String, LiteralFactory> entry : strategies.entrySet()) {
            if (userInput.matches(entry.getKey())) {
                return entry.getValue().createLiteral(userInput, start, end);
            }
        }
        throw new IllegalArgumentException("Can't process your input: " + userInput);
    }
}
