package linter.factory;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import linter.TestUtils;
import linter.engine.factory.IdentifierStrategyFactory;
import linter.engine.factory.StrategyFactory;
import linter.engine.strategy.LintingStrategy;
import linter.engine.strategy.StrategiesContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IdentifierStrategyFactoryTest {
  @Test
  public void testCreateStrategies() {
    String rules = TestUtils.readResourceFile("linterRulesExampleReworkA.json");
    assertNotNull(rules);

    StrategyFactory factory = new IdentifierStrategyFactory();

    LintingStrategy strategies = factory.createStrategies(rules, "1.0");

    assertNotNull(strategies);
    Assertions.assertInstanceOf(StrategiesContainer.class, strategies);
  }
}
