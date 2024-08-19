package visitors;

import ast.expressions.Expression;
import ast.identifier.Identifier;
import ast.root.ASTNode;
import com.google.gson.JsonParser;
import java.util.List;
import java.util.regex.Pattern;

public class LinterVisitor implements ASTVisitor {
    private final String report;
    private final String rules;

    public LinterVisitor(String rules) {
        this.report = "";
        this.rules = rules;
    }

    public LinterVisitor(String report, String rules) {
        this.report = report;
        this.rules = rules;
    }

    public String getReport() {
        return report;
    }

    @Override
    public ASTVisitor visitVarDec(Identifier identifier, Expression expression) {
        String getIdentifierReport = verifyIdentifier(identifier);
        String newReport = getReport();
        if (!getIdentifierReport.isEmpty()) {
            newReport += getIdentifierReport + "\n";
        }
        return new LinterVisitor(newReport, rules);
    }

    @Override
    public ASTVisitor visitCallExpression(Identifier identifier, List<ASTNode> arguments) {
        return null;
    }

    @Override
    public ASTVisitor visitAssignmentExpression(Identifier identifier, String operator, Expression expression) {
        return null;
    }

    private String verifyIdentifier(Identifier identifier) {
        String getWritingConvention = JsonParser
                .parseString(rules)
                .getAsJsonObject()
                .getAsJsonObject("identifier")
                .get("writingConvention")
                .getAsString();

        if (getWritingConvention.equals("camelCase")) {
            return verifyCamelCase(identifier);
        } else if (getWritingConvention.equals("snakeCase")) {
            return verifySnakeCase(identifier);
        }

        return "";
    }

    private String verifyCamelCase(Identifier identifier) {
        String identifierName = identifier.name();
        if (Pattern.matches("^[a-z]+([A-Z][a-z]+)*$", identifierName)) {
            return "";
        }
        return "Warning from " + identifier.start() + " to " + identifier.end() + ": Identifier " + identifierName + " does not follow camelCase convention";
    }

    private String verifySnakeCase(Identifier identifier) {
        String identifierName = identifier.name();
        if (Pattern.matches("^[a-z]+(_[a-z]+)*$", identifierName)) {
            return "";
        }
        return "Warning from " + identifier.start() + " to " + identifier.end() + ": Identifier " + identifierName + " does not follow snake_case convention";
    }
}
