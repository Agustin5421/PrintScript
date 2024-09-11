package formatter.strategy.common;

import ast.root.AstNode;
import formatter.strategy.FormattingStrategy;
import formatter.visitor.FormatterVisitor;

public class CharacterStrategy implements FormattingStrategy {
  private final String character;

  public CharacterStrategy(String character) {
    this.character = character;
  }

  @Override
  public String apply(AstNode node, FormatterVisitor visitor) {
    return character;
  }
}
