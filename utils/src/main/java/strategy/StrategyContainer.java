package strategy;

import java.util.Map;

public class StrategyContainer<K, V> {
  private final Map<K, V> strategies;
  private final String errorMessage;

  public StrategyContainer(Map<K, V> strategies, String errorMessage) {
    this.strategies = strategies;
    this.errorMessage = errorMessage;
  }

  // This way we ensure that this will never return a null strategy.
  public V getStrategy(K key) {
    V interpretingStrategy = strategies.get(key);
    if (interpretingStrategy == null) {
      throw new IllegalArgumentException(errorMessage + " " + key + ".");
    }
    return interpretingStrategy;
  }
}
