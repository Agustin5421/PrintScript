package formatter.strategy.vardec;

import ast.root.AstNode;
import ast.statements.VariableDeclaration;
import formatter.FormatterVisitor;
import formatter.strategy.FormattingStrategy;
import java.util.List;

public class VariableDeclarationStrategy implements FormattingStrategy {
  // Strategies should be having (or not) the first space, : and the
  // second space, then the nodeType of the node
  // then the assignment strategy
  private final List<FormattingStrategy> strategies;
  // Keyword that for now is only let
  private final String keyword;

  public VariableDeclarationStrategy(List<FormattingStrategy> strategies, String keyword) {
    this.strategies = strategies;
    this.keyword = keyword;
  }

  @Override
  public String apply(AstNode node, FormatterVisitor visitor) {
    VariableDeclaration varDecNode = (VariableDeclaration) node;
    StringBuilder formattedCode = new StringBuilder();
    formattedCode.append(keyword).append(" ");
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
