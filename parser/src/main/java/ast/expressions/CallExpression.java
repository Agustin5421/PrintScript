package ast.expressions;

import ast.Expression;
import ast.Identifier;
import java.util.List;

public class CallExpression implements ExpressionType {
    private final Identifier identifier;
    private final List<Expression> arguments;
    private final boolean optionalParameters;

    public CallExpression(Identifier identifier, List<Expression> arguments, boolean optionalParameters) {
        this.identifier = identifier;
        this.arguments = arguments;
        this.optionalParameters = optionalParameters;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public List<Expression> getArguments() {
        return arguments;
    }

    public boolean hasOptionalParameters() {
        return optionalParameters;
    }
}
