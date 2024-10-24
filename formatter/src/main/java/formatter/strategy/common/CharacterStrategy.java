package formatter.strategy.common;

import ast.root.AstNode;
import formatter.FormattingEngine;
import formatter.strategy.FormattingStrategy;

public class CharacterStrategy implements FormattingStrategy {
  private final String character;
  private final int repetitions;

  public CharacterStrategy(String character, int repetitions) {
    this.character = character;
    this.repetitions = repetitions;
  }

  public CharacterStrategy(String character) {
    this.character = character;
    this.repetitions = 1;
  }

  @Override
  public FormattingEngine apply(AstNode node, FormattingEngine engine) {
    engine.write(character.repeat(repetitions));
    return engine;
  }
}
