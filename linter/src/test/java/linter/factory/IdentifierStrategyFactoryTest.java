package linter.factory;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import linter.TestUtils;
import linter.visitor.factory.IdentifierStrategyFactory;
import linter.visitor.factory.StrategyFactory;
import linter.visitor.strategy.LintingStrategy;
import linter.visitor.strategy.StrategiesContainer;
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
