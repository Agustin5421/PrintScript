package interpreter.visitor.strategy;

import java.util.Map;

public class StrategyContainer<T> {
  private final Map<T, InterpretingStrategy> strategies;

  public StrategyContainer(Map<T, InterpretingStrategy> strategies) {
    this.strategies = strategies;
  }

  // This way we ensure that this will never return a null strategy.
  public InterpretingStrategy getStrategy(T key) {
    InterpretingStrategy interpretingStrategy = strategies.get(key);
    if (interpretingStrategy == null) {
      throw new IllegalArgumentException("Can't interpret " + key + ".");
    }
    return interpretingStrategy;
  }
}
