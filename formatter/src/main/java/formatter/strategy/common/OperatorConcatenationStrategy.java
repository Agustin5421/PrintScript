package formatter.strategy.common;

import ast.root.AstNode;
import formatter.strategy.FormattingStrategy;
import formatter.visitor.FormatterVisitor;
import java.util.List;

public class OperatorConcatenationStrategy implements FormattingStrategy {
  // The strategies consist of whitespaces or the missing of it and
  // the operator concatenation
  private final List<CharacterStrategy> strategies;

  public OperatorConcatenationStrategy(List<CharacterStrategy> strategies) {
    this.strategies = strategies;
  }

  @Override
  public String apply(AstNode node, FormatterVisitor visitor) {
    StringBuilder formattedCode = new StringBuilder();
    for (FormattingStrategy strategy : strategies) {
      formattedCode.append(strategy.apply(node, visitor));
    }
    return formattedCode.toString();
  }
}
