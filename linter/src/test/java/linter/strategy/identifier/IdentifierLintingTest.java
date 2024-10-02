package linter.strategy.identifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ast.identifier.Identifier;
import ast.root.AstNodeType;
import java.util.List;
import java.util.Map;
import linter.engine.LinterEngine;
import linter.engine.strategy.LintingStrategy;
import linter.engine.strategy.StrategiesContainer;
import linter.engine.strategy.identifier.WritingConventionStrategy;
import org.junit.jupiter.api.Test;
import output.OutputReport;
import position.Position;
import report.Report;
import strategy.StrategyContainer;

public class IdentifierLintingTest {
  private LinterEngine getLinterEngine(LintingStrategy mainIdLinting) {
    StrategyContainer<AstNodeType, LintingStrategy> nodesStrategies =
        new StrategyContainer<>(Map.of(AstNodeType.IDENTIFIER, mainIdLinting), "Can't lint: ");
    OutputReport output = new OutputReport();
    return new LinterEngine(nodesStrategies, output);
  }

  private List<Identifier> getIdentifiers() {
    Position camelStart = new Position(0, 0);
    Position camelEnd = new Position(0, 8);
    Identifier camelIdentifier = new Identifier("testName", camelStart, camelEnd);

    Position snakeStart = new Position(1, 0);
    Position snakeEnd = new Position(1, 8);
    Identifier snakeIdentifier = new Identifier("test_name", snakeStart, snakeEnd);

    return List.of(camelIdentifier, snakeIdentifier);
  }

  @Test
  public void severalIdentifiersCamelCaseTest() {
    List<Identifier> identifiers = getIdentifiers();

    LintingStrategy camelCaseIdentifier =
        new WritingConventionStrategy("camelCase", "^[a-z]+(?:[A-Z]?[a-z0-9]+)*$");
    LintingStrategy mainIdLinting = new StrategiesContainer(List.of(camelCaseIdentifier));

    LinterEngine engine = getLinterEngine(mainIdLinting);

    for (Identifier identifier : identifiers) {
      engine.lintNode(identifier);
    }

    List<Report> result = ((OutputReport) engine.getOutput()).getFullReport().getReports();
    assertEquals(1, result.size());
  }

  @Test
  public void severalIdentifiersSnakeCaseTest() {
    List<Identifier> identifiers = getIdentifiers();

    LintingStrategy snakeCaseIdentifier =
        new WritingConventionStrategy("snakeCase", "^[a-z]+(_[a-z0-9]+)*$");
    LintingStrategy mainIdLinting = new StrategiesContainer(List.of(snakeCaseIdentifier));

    LinterEngine engine = getLinterEngine(mainIdLinting);

    for (Identifier identifier : identifiers) {
      engine.lintNode(identifier);
    }

    List<Report> result = ((OutputReport) engine.getOutput()).getFullReport().getReports();
    assertEquals(1, result.size());
  }

  @Test
  public void severalIdentifiersBothCaseTest() {
    List<Identifier> identifiers = getIdentifiers();

    LintingStrategy camelCaseIdentifier =
        new WritingConventionStrategy("camelCase", "^[a-z]+(?:[A-Z]?[a-z0-9]+)*$");
    LintingStrategy snakeCaseIdentifier =
        new WritingConventionStrategy("snakeCase", "^[a-z]+(_[a-z0-9]+)*$");
    LintingStrategy mainIdLinting =
        new StrategiesContainer(List.of(camelCaseIdentifier, snakeCaseIdentifier));

    LinterEngine engine = getLinterEngine(mainIdLinting);

    for (Identifier identifier : identifiers) {
      engine.lintNode(identifier);
    }

    List<Report> result = ((OutputReport) engine.getOutput()).getFullReport().getReports();
    assertEquals(2, result.size());
  }
}
