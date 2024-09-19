package formatter.strategy.common;

import ast.root.AstNode;
import formatter.FormattingEngine;
import formatter.strategy.FormattingStrategy;

public class CharacterStrategy implements FormattingStrategy {
  private final String character;

  public CharacterStrategy(String character) {
    this.character = character;
  }

  @Override
  public FormattingEngine apply(AstNode node, FormattingEngine engine) {
    engine.write(character);
    return engine;
  }
}
