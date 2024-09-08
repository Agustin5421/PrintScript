package formatter.strategy.reassign;

import ast.root.AstNode;
import ast.statements.AssignmentExpression;
import formatter.strategy.FormattingStrategy;
import formatter.strategy.common.OperatorConcatenationStrategy;
import formatter.visitor.FormatterVisitorV1;

public class ReAssignationStrategy implements FormattingStrategy {
  // The equal assignation strategy
  private final OperatorConcatenationStrategy strategy;

  public ReAssignationStrategy(OperatorConcatenationStrategy strategy) {
    this.strategy = strategy;
  }

  @Override
  public String apply(AstNode node, FormatterVisitorV1 visitor) {
    AssignmentExpression assignmentExpression = (AssignmentExpression) node;
    StringBuilder formattedCode = new StringBuilder();
    // Adding the identifier
    FormatterVisitorV1 visit = (FormatterVisitorV1) assignmentExpression.left().accept(visitor);
    formattedCode.append(visit.getCurrentCode());
    formattedCode.append(strategy.apply(node, visitor));
    // Formatting the expression
    formattedCode.append(
        ((FormatterVisitorV1) assignmentExpression.right().accept(visitor)).getCurrentCode());
    formattedCode.append(";");
    return formattedCode.toString();
  }
}
