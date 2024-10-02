package linter.strategy.identifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ast.identifier.Identifier;
import ast.root.AstNodeType;
import java.util.List;
import java.util.Map;
import linter.engine.LinterEngine;
import linter.engine.strategy.LintingStrategy;
import linter.engine.strategy.identifier.WritingConventionStrategy;
import org.junit.jupiter.api.Test;
import output.OutputReport;
import position.Position;
import report.Report;
import strategy.StrategyContainer;

public class CamelCaseTest {
  @Test
  public void identifierIsInCamelCase() {
    Position start = new Position(0, 0);
    Position end = new Position(0, 8);
    Identifier identifier = new Identifier("testName", start, end);

    LintingStrategy camelCaseIdentifier =
        new WritingConventionStrategy("camelCase", "^[a-z]+(?:[A-Z]?[a-z0-9]+)*$");

    StrategyContainer<AstNodeType, LintingStrategy> nodesStrategies =
        new StrategyContainer<>(
            Map.of(AstNodeType.IDENTIFIER, camelCaseIdentifier), "Can't lint: ");
    OutputReport output = new OutputReport();
    LinterEngine engine = new LinterEngine(nodesStrategies, output);

    engine.lintNode(identifier);

    List<Report> result = output.getFullReport().getReports();
    assertEquals(0, result.size());
  }

  @Test
  public void identifierIsNotInCamelCase() {
    Position start = new Position(0, 0);
    Position end = new Position(0, 8);
    Identifier identifier = new Identifier("test_name", start, end);

    LintingStrategy camelCaseIdentifier =
        new WritingConventionStrategy("camelCase", "^[a-z]+(?:[A-Z]?[a-z0-9]+)*$");

    StrategyContainer<AstNodeType, LintingStrategy> nodesStrategies =
        new StrategyContainer<>(
            Map.of(AstNodeType.IDENTIFIER, camelCaseIdentifier), "Can't lint: ");
    OutputReport output = new OutputReport();
    LinterEngine engine = new LinterEngine(nodesStrategies, output);

    engine.lintNode(identifier);

    List<Report> result = output.getFullReport().getReports();

    assertEquals(1, result.size());
  }
}
