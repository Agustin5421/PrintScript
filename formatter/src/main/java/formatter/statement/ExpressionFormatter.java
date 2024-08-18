package formatter.statement;

import ast.expressions.BinaryExpression;
import ast.expressions.Expression;
import ast.identifier.Identifier;
import ast.literal.Literal;
import ast.root.ASTNode;
import com.google.gson.JsonObject;

public class ExpressionFormatter implements Formatter {
    @Override
    // For now, it has private uses
    public boolean shouldFormat(ASTNode statement) {
        return false;
    }

    @Override
    public String format(ASTNode node, JsonObject rules, String currentProgram) {
        Expression expressionNode = (Expression) node;
        String formattedCode = "";
        formattedCode = evaluate(expressionNode, formattedCode, rules);
        return formattedCode;
    }

    private String evaluate(Expression expression, String formattedCode, JsonObject rules) {
        if (expression instanceof BinaryExpression) {
            return handleBinaryExpression((BinaryExpression) expression, formattedCode, rules);
        } else if (expression instanceof Literal<?>) {
            return formattedCode + ((Literal<?>) expression).value();
        } else if (expression instanceof Identifier) {
            return formattedCode + cleanIdentifier(((Identifier) expression));
        } else {
            throw new IllegalArgumentException("Undefined expression");
        }
    }

    private String handleBinaryExpression(BinaryExpression expression, String formattedCode, JsonObject rules) {
        formattedCode = evaluate(expression.left(), formattedCode, rules);
        formattedCode += " " + expression.operator() + " ";
        return evaluate(expression.right(), formattedCode, rules);
    }

    // Removing spaces and line breaks
    private String cleanIdentifier(Identifier identifier) {
        return identifier.name().replaceAll("\\s+", "");
    }
}
