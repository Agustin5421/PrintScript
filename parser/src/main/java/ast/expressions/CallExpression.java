package ast.expressions;

import ast.Expression;
import ast.Identifier;
import java.util.List;


public record CallExpression(Identifier identifier, List<Expression> arguments, boolean optionalParameters) implements ExpressionType {
}