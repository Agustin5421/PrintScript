package formatter.strategy.reassign;

import ast.root.AstNode;
import ast.statements.AssignmentExpression;
import formatter.FormattingEngine;
import formatter.strategy.FormattingStrategy;
import formatter.strategy.common.AssignationStrategy;

public class ReAssignationStrategy implements FormattingStrategy {
  private final AssignationStrategy assignationStrategy;

  public ReAssignationStrategy(AssignationStrategy assignationStrategy) {
    this.assignationStrategy = assignationStrategy;
  }

  @Override
  public FormattingEngine apply(AstNode node, FormattingEngine engine) {
    AssignmentExpression assignmentExpression = (AssignmentExpression) node;

    // Adding the identifier
    engine.format(assignmentExpression.left());

    // Assigning the expression
    assignationStrategy.apply(assignmentExpression.right(), engine);

    engine.write(";\n");

    return engine;
  }
}
