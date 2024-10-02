package linter.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ast.identifier.Identifier;
import ast.root.AstNodeType;
import java.util.List;
import java.util.Map;
import linter.engine.strategy.LintingStrategy;
import linter.engine.strategy.StrategiesContainer;
import linter.engine.strategy.identifier.WritingConventionStrategy;
import org.junit.jupiter.api.Test;
import output.OutputListString;
import position.Position;
import strategy.StrategyContainer;

public class InitialLinterEngineV1Test {
  private LinterEngine getLinterEngineV2() {
    LintingStrategy idStrategy =
        new WritingConventionStrategy("camelCase", "^[a-z]+(?:[A-Z]?[a-z0-9]+)*$");
    LintingStrategy mainIdStrategy = new StrategiesContainer(List.of(idStrategy));

    Map<AstNodeType, LintingStrategy> nodesStrategies =
        Map.of(AstNodeType.IDENTIFIER, mainIdStrategy);

    StrategyContainer<AstNodeType, LintingStrategy> mockStrategy =
        new StrategyContainer<>(nodesStrategies, "Can't lint: ");

    return new LinterEngine(mockStrategy, new OutputListString());
  }

  @Test
  public void testMutability() {
    LinterEngine engine = getLinterEngineV2();

    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("test_name", position, position);

    LinterEngine newEngine = engine.lintNode(identifier);

    OutputListString oldOutput = (OutputListString) engine.getOutput();
    OutputListString newOutput = (OutputListString) newEngine.getOutput();

    assertEquals(1, oldOutput.getSavedResults().size());
    assertEquals(1, newOutput.getSavedResults().size());
  }

  @Test
  public void testNoViolations() {
    LinterEngine engine = getLinterEngineV2();

    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("testName", position, position);

    LinterEngine newEngine = engine.lintNode(identifier);

    OutputListString output = (OutputListString) newEngine.getOutput();

    assertEquals(0, output.getSavedResults().size());
  }

  @Test
  public void testMultipleViolations() {
    LinterEngine engine = getLinterEngineV2();

    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("test_name", position, position);

    LinterEngine newEngine = engine.lintNode(identifier);
    LinterEngine newEngine2 = newEngine.lintNode(identifier);

    OutputListString output = (OutputListString) newEngine2.getOutput();

    assertEquals(2, output.getSavedResults().size());
  }
}
