package linter.strategy.identifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ast.identifier.Identifier;
import ast.root.AstNodeType;
import java.util.List;
import java.util.Map;
import linter.engine.LinterEngine;
import linter.engine.report.FullReport;
import linter.engine.report.Report;
import linter.engine.strategy.LintingStrategy;
import linter.engine.strategy.identifier.WritingConventionStrategy;
import org.junit.jupiter.api.Test;
import output.OutputListString;
import output.OutputString;
import strategy.StrategyContainer;
import token.Position;

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
    OutputString output = new OutputString();
    LinterEngine engine = new LinterEngine(nodesStrategies, output);

    engine.lintNode(identifier);

    String result = output.getResult();
    assertEquals("", result);
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
    OutputListString output = new OutputListString();
    LinterEngine engine = new LinterEngine(nodesStrategies, output);

    engine.lintNode(identifier);

    List<String> result = output.getSavedResults();

    assertEquals(1, result.size());

    FullReport fullReport = new FullReport();
    fullReport = camelCaseIdentifier.oldApply(identifier, fullReport);

    assertEquals(1, fullReport.getReports().size());

    Report report = fullReport.getReports().get(0);
    assertEquals(start, report.start());
    assertEquals(end, report.end());
  }
}
