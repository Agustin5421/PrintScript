package formatter.strategy.common.space;

import formatter.strategy.common.CharacterStrategy;

public class WhiteSpace extends CharacterStrategy {
  public WhiteSpace(int repetitions) {
    super(" ", repetitions);
  }

  public WhiteSpace() {
    super(" ");
  }
}
