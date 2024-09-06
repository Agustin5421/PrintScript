package formatter.strategy.reassign;

import ast.root.AstNode;
import ast.statements.AssignmentExpression;
import formatter.FormatterVisitor;
import formatter.strategy.FormattingStrategy;
import formatter.strategy.common.OperatorConcatenationStrategy;

public class ReAssignationStrategy implements FormattingStrategy {
  // The equal assignation strategy
  private final OperatorConcatenationStrategy strategy;

  public ReAssignationStrategy(OperatorConcatenationStrategy strategy) {
    this.strategy = strategy;
  }

  @Override
  public String apply(AstNode node, FormatterVisitor visitor) {
    AssignmentExpression assignmentExpression = (AssignmentExpression) node;
    StringBuilder formattedCode = new StringBuilder();
    // Adding the identifier
    FormatterVisitor visit = (FormatterVisitor) assignmentExpression.left().accept(visitor);
    formattedCode.append(visit.getCurrentCode());
    formattedCode.append(strategy.apply(node, visitor));
    // Formatting the expression
    formattedCode.append(
        ((FormatterVisitor) assignmentExpression.right().accept(visitor)).getCurrentCode());
    formattedCode.append(";");
    return formattedCode.toString();
  }
}
