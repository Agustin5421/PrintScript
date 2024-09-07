package formatter.strategy.vardec;

import ast.root.AstNode;
import formatter.FormatterVisitor;
import formatter.strategy.FormattingStrategy;
import java.util.List;

public class TypingStrategy implements FormattingStrategy {
  private final List<FormattingStrategy> strategies;

  public TypingStrategy(List<FormattingStrategy> strategies) {
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