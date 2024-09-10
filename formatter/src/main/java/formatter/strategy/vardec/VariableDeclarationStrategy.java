package formatter.strategy.vardec;

import ast.root.AstNode;
import ast.statements.VariableDeclaration;
import formatter.strategy.FormattingStrategy;
import formatter.visitor.FormatterVisitor;
import java.util.List;

public class VariableDeclarationStrategy implements FormattingStrategy {
  // Strategies should be having (or not) the first space, : and the
  // second space, then the nodeType of the node
  // then the assignment strategy
  private final List<FormattingStrategy> strategies;

  public VariableDeclarationStrategy(List<FormattingStrategy> strategies) {
    this.strategies = strategies;
  }

  @Override
  public String apply(AstNode node, FormatterVisitor visitor) {
    VariableDeclaration varDecNode = (VariableDeclaration) node;
    StringBuilder formattedCode = new StringBuilder();
    formattedCode.append(varDecNode.kind()).append(" ");
    // Adding the identifier
    FormatterVisitor visit = (FormatterVisitor) varDecNode.identifier().accept(visitor);
    formattedCode.append(visit.getCurrentCode());
    // Adding the whitespaces strategies
    for (FormattingStrategy strategy : strategies) {
      formattedCode.append(strategy.apply(varDecNode, visitor));
    }
    // Formatting the expression
    formattedCode.append(
        ((FormatterVisitor) varDecNode.expression().accept(visitor)).getCurrentCode());
    formattedCode.append(";");
    return formattedCode.toString();
  }
}
