package formatter.condition;

import ast.root.ASTNode;
import ast.statements.CallExpression;
import com.google.gson.JsonObject;

// For now, hardcoded for println
public class FunctionCallFormatter implements Formatter {
    private final ExpressionFormatter expressionFormatter;

    public FunctionCallFormatter(ExpressionFormatter expressionFormatter) {
        this.expressionFormatter = expressionFormatter;
    }

    @Override
    public boolean shouldFormat(ASTNode statement) {
        return statement instanceof CallExpression;
    }

    @Override
    public String format(ASTNode node, JsonObject rules, String currentProgram) {
        CallExpression callExpressionNode = (CallExpression) node;
        StringBuilder formattedCode;
        int linesBreak = rules.getAsJsonObject("printLineBreaks").getAsInt();

        formattedCode = addOrRemoveLineBreaks(currentProgram, linesBreak);

        formattedCode.append(callExpressionNode.methodIdentifier());
        for (ASTNode argument: callExpressionNode.arguments()) {
            formattedCode.append(expressionFormatter.format(argument, rules, currentProgram));
        }
        return formattedCode.toString();
    }

    private StringBuilder addOrRemoveLineBreaks(String currentProgram, int linesBreak) {
        StringBuilder lineBreaks = new StringBuilder();
        // Count the number of line breaks at the end of currentProgram
        int currentLineBreaks = 0;
        for (int i = currentProgram.length() - 1; i >= 0 && currentProgram.charAt(i) == '\n'; i--) {
            currentLineBreaks++;
        }

        // Append the necessary number of line breaks
        lineBreaks.append("\n".repeat(Math.max(0, linesBreak - currentLineBreaks)));
        return lineBreaks;
    }
}
