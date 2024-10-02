package linter.engine.strategy.callexpression;

import ast.root.AstNode;
import ast.root.AstNodeType;
import ast.statements.CallExpression;
import java.util.List;
import linter.engine.LinterEngine;
import linter.engine.strategy.LintingStrategy;
import report.Report;

public class ArgumentsStrategy implements LintingStrategy {
  private final List<AstNodeType> allowedArguments;

  public ArgumentsStrategy(List<AstNodeType> allowedArguments) {
    this.allowedArguments = allowedArguments;
  }

  @Override
  public LinterEngine apply(AstNode node, LinterEngine engine) {
    if (!shouldApply(node)) {
      return engine;
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

        engine.getOutput().saveResult(newReport);
      }
    }

    return engine;
  }

  private boolean shouldApply(AstNode node) {
    return node.getNodeType().equals(AstNodeType.CALL_EXPRESSION);
  }

  private boolean isNotValidArgument(AstNodeType argumentType) {
    return !allowedArguments.contains(argumentType);
  }
}
