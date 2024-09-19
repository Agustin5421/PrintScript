package linter.visitor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ast.identifier.Identifier;
import ast.root.AstNodeType;
import java.util.List;
import java.util.Map;
import linter.visitor.strategy.LintingStrategy;
import linter.visitor.strategy.NewLinterVisitor;
import linter.visitor.strategy.StrategiesContainer;
import linter.visitor.strategy.identifier.WritingConventionStrategy;
import org.junit.jupiter.api.Test;
import output.OutputListString;
import strategy.StrategyContainer;
import token.Position;

public class InitialLinterVisitorV1Test {
  private NewLinterVisitor getLinterVisitorV2() {
    LintingStrategy idStrategy =
        new WritingConventionStrategy("camelCase", "^[a-z]+(?:[A-Z]?[a-z0-9]+)*$");
    LintingStrategy mainIdStrategy = new StrategiesContainer(List.of(idStrategy));

    Map<AstNodeType, LintingStrategy> nodesStrategies =
        Map.of(AstNodeType.IDENTIFIER, mainIdStrategy);

    StrategyContainer<AstNodeType, LintingStrategy> mockStrategy =
        new StrategyContainer<>(nodesStrategies, "Can't lint: ");

    return new ReworkedLinterVisitor(mockStrategy, new OutputListString());
  }

  @Test
  public void testMutability() {
    NewLinterVisitor visitor = getLinterVisitorV2();

    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("test_name", position, position);

    NewLinterVisitor newVisitor = visitor.lintNode(identifier);

    OutputListString oldOutput = (OutputListString) visitor.getOutput();
    OutputListString newOutput = (OutputListString) newVisitor.getOutput();

    assertEquals(1, oldOutput.getSavedResults().size());
    assertEquals(1, newOutput.getSavedResults().size());
  }

  @Test
  public void testNoViolations() {
    NewLinterVisitor visitor = getLinterVisitorV2();

    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("testName", position, position);

    NewLinterVisitor newVisitor = visitor.lintNode(identifier);

    OutputListString output = (OutputListString) newVisitor.getOutput();

    assertEquals(0, output.getSavedResults().size());
  }

  @Test
  public void testMultipleViolations() {
    NewLinterVisitor visitor = getLinterVisitorV2();

    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("test_name", position, position);

    NewLinterVisitor newVisitor = visitor.lintNode(identifier);
    NewLinterVisitor newVisitor2 = newVisitor.lintNode(identifier);

    OutputListString output = (OutputListString) newVisitor2.getOutput();

    assertEquals(2, output.getSavedResults().size());
  }
}
