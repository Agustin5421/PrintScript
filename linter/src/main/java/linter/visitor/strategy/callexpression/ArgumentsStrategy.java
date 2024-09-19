package linter.visitor.strategy.callexpression;

import ast.root.AstNode;
import ast.root.AstNodeType;
import ast.statements.CallExpression;
import java.util.List;
import linter.visitor.LinterVisitor;
import linter.visitor.report.FullReport;
import linter.visitor.report.Report;
import linter.visitor.strategy.LintingStrategy;

public class ArgumentsStrategy implements LintingStrategy {
  private final List<AstNodeType> allowedArguments;

  public ArgumentsStrategy(List<AstNodeType> allowedArguments) {
    this.allowedArguments = allowedArguments;
  }

  @Override
  public FullReport apply(AstNode node, FullReport fullReport) {
    if (!shouldApply(node)) {
      return fullReport;
    }

    CallExpression callExpression = (CallExpression) node;

    List<AstNode> arguments = callExpression.arguments();

    for (AstNode argument : arguments) {
      AstNodeType argumentType = argument.getNodeType();
      if (isNotValidArgument(argumentType)) {
        Report newReport =
            new Report(
                argument.start(),
                argument.end(),
                "Value of type " + argumentType + " is not allowed as an argument");
        fullReport = fullReport.addReport(newReport);
      }
    }

    return fullReport;
  }

  @Override
  public LinterVisitor apply(AstNode node, LinterVisitor visitor) {
    if (!shouldApply(node)) {
      return visitor;
    }

    CallExpression callExpression = (CallExpression) node;

    List<AstNode> arguments = callExpression.arguments();

    for (AstNode argument : arguments) {
      AstNodeType argumentType = argument.getNodeType();
      if (isNotValidArgument(argumentType)) {
        Report newReport =
            new Report(
                argument.start(),
                argument.end(),
                "Value of type " + argumentType + " is not allowed as an argument");

        visitor.getOutput().saveResult(newReport.toString());
      }
    }

    return visitor;
  }

  private boolean shouldApply(AstNode node) {
    return node.getNodeType().equals(AstNodeType.CALL_EXPRESSION);
  }

  private boolean isNotValidArgument(AstNodeType argumentType) {
    return !allowedArguments.contains(argumentType);
  }
}
