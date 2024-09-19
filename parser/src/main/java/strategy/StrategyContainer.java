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
    return strategies.get(key);
  }
}
