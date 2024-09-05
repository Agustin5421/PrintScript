package formatter.newimpl.strategy;

import ast.root.AstNode;
import ast.statements.AssignmentExpression;
import formatter.newimpl.FormatterVisitor2;

public class ReAssignationStrategy implements FormattingStrategy {
  // The equal assignation strategy
  private final OperatorConcatenationStrategy strategy;

  public ReAssignationStrategy(OperatorConcatenationStrategy strategy) {
    this.strategy = strategy;
  }

  @Override
  public String apply(AstNode node, FormatterVisitor2 visitor) {
    AssignmentExpression assignmentExpression = (AssignmentExpression) node;
    StringBuilder formattedCode = new StringBuilder();
    // Adding the identifier
    FormatterVisitor2 visit = (FormatterVisitor2) assignmentExpression.left().accept(visitor);
    formattedCode.append(visit.getCurrentCode());
    formattedCode.append(strategy.apply(node, visitor));
    // Formatting the expression
    formattedCode.append(
        ((FormatterVisitor2) assignmentExpression.right().accept(visitor)).getCurrentCode());
    formattedCode.append(";");
    return formattedCode.toString();
  }
}
