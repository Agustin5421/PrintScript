package formatter.statement;

import ast.identifier.Identifier;
import ast.root.AstNode;
import ast.statements.CallExpression;
import com.google.gson.JsonObject;

// For now, hardcoded for println
public class FunctionCallFormatter implements Formatter {
  private final ExpressionFormatter expressionFormatter;

  public FunctionCallFormatter(ExpressionFormatter expressionFormatter) {
    this.expressionFormatter = expressionFormatter;
  }

  @Override
  public String format(AstNode node, JsonObject rules, String currentProgram) {
    CallExpression callExpressionNode = (CallExpression) node;
    StringBuilder formattedCode;
    int linesBreak = rules.get("printLineBreaks").getAsInt() + 1;

    formattedCode = addOrRemoveLineBreaks(currentProgram, linesBreak);

    formattedCode.append(cleanIdentifier(callExpressionNode.methodIdentifier())).append("(");
    for (AstNode argument : callExpressionNode.arguments()) {
      formattedCode.append(expressionFormatter.format(argument, rules, currentProgram));
    }
    formattedCode.append(");");
    return formattedCode.toString();
  }

  private StringBuilder addOrRemoveLineBreaks(String currentProgram, int linesBreak) {
    StringBuilder lineBreaks = new StringBuilder();
    // Check first if we are the start of the program
    if (!currentProgram.isEmpty()) {
      // Count the number of line breaks at the end of currentProgram
      int currentLineBreaks = 0;
      for (int i = currentProgram.length() - 1; i >= 0 && currentProgram.charAt(i) == '\n'; i--) {
        currentLineBreaks++;
      }

      // Append the necessary number of line breaks
      lineBreaks.append("\n".repeat(Math.max(0, linesBreak - currentLineBreaks)));
    }
    return lineBreaks;
  }

  // Removing spaces and line breaks
  private String cleanIdentifier(Identifier identifier) {
    return identifier.name().replaceAll("\\s+", "");
  }
}
