package formatter.strategy.reassign;

import ast.root.AstNode;
import ast.statements.AssignmentExpression;
import formatter.strategy.FormattingStrategy;
import formatter.strategy.common.AssignationStrategy;
import formatter.visitor.FormatterVisitor;

public class ReAssignationStrategy implements FormattingStrategy {
  private final AssignationStrategy assignationStrategy;

  public ReAssignationStrategy(AssignationStrategy assignationStrategy) {
    this.assignationStrategy = assignationStrategy;
  }

  @Override
  public String apply(AstNode node, FormatterVisitor visitor) {
    AssignmentExpression assignmentExpression = (AssignmentExpression) node;
    StringBuilder formattedCode = new StringBuilder();

    // Adding the identifier
    FormatterVisitor visit = (FormatterVisitor) assignmentExpression.left().accept(visitor);
    formattedCode.append(visit.getCurrentCode());

    // Assigning the expression
    formattedCode.append(assignationStrategy.apply(assignmentExpression.right(), visitor));
    formattedCode.append(";");

    return formattedCode.toString();
  }
}
