package formatter.condition;

import ast.expressions.BinaryExpression;
import ast.expressions.Expression;
import ast.identifier.Identifier;
import ast.literal.StringLiteral;
import ast.root.ASTNode;
import ast.statements.VariableDeclaration;
import com.google.gson.JsonObject;

public class VariableDeclarationFormatter implements Formatter {
    private final ExpressionFormatter expressionFormatter;

    public VariableDeclarationFormatter(ExpressionFormatter expressionFormatter) {
        this.expressionFormatter = expressionFormatter;
    }

    @Override
    public boolean shouldFormat(ASTNode statement) {
        return statement instanceof VariableDeclaration;
    }

    @Override
    public String format(ASTNode node, JsonObject rules, String currentProgram) {
        VariableDeclaration varDeclarationNode = (VariableDeclaration) node;
        StringBuilder formattedCode = new StringBuilder();
        JsonObject colonRules = rules.getAsJsonObject("colonRules");
        boolean beforeSpace = colonRules.getAsJsonObject("before").getAsBoolean();
        boolean afterSpace = colonRules.getAsJsonObject("after").getAsBoolean();
        boolean equalSpace = rules.getAsJsonObject("equalSpace").getAsBoolean();

        formattedCode.append("let ")
                .append(cleanIdentifier(varDeclarationNode.identifier()))
                .append(beforeSpace ? " " : "")
                .append(":")
                .append(afterSpace ? " ": "")
                .append(getType(varDeclarationNode.expression()))
                .append(equalSpace ? " = " : "=")
                .append(expressionFormatter.format(varDeclarationNode.expression(), rules, currentProgram));
        return  formattedCode.toString();
    }

    // Removing spaces and line breaks
    private String cleanIdentifier(Identifier identifier) {
        return identifier.name().replaceAll("\\s+", "");
    }

    // Returning the type equivalent of printscript (only working for string and numbers for now)
    private String getType(Expression expression) throws IllegalArgumentException {
        if (containsStringLiteral(expression)) {
            return "string";
        }
        return "number";
    }

    private boolean containsStringLiteral(Expression expression) {
        if (expression instanceof StringLiteral) {
            return true;
        } else if (expression instanceof BinaryExpression binaryExpression) {
            return containsStringLiteral(binaryExpression.left()) || containsStringLiteral(binaryExpression.right());
        }
        return false;
    }
}
