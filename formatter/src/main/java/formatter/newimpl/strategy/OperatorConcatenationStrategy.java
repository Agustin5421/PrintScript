package formatter.newimpl.strategy;

import ast.root.AstNode;
import formatter.newimpl.FormatterVisitor2;
import java.util.List;

public class OperatorConcatenationStrategy implements FormattingStrategy {
  // The strategies consist of whitespaces or the missing of it and
  // the operator concatenation
  private final List<FormattingStrategy> strategies;

  public OperatorConcatenationStrategy(List<FormattingStrategy> strategies) {
    this.strategies = strategies;
  }

  @Override
  public String apply(AstNode node, FormatterVisitor2 visitor) {
    StringBuilder formattedCode = new StringBuilder();
    for (FormattingStrategy strategy : strategies) {
      formattedCode.append(strategy.apply(node, visitor));
    }
    return formattedCode.toString();
  }
}
