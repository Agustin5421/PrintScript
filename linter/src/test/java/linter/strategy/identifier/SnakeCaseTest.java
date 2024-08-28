package linter.strategy.identifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ast.identifier.Identifier;
import linter.report.FullReport;
import linter.report.Report;
import linter.visitor.strategy.LintingStrategy;
import linter.visitor.strategy.identifier.SnakeCaseIdentifier;
import org.junit.jupiter.api.Test;
import token.Position;

public class SnakeCaseTest {
  @Test
  public void identifierIsInSnakeCase() {
    Position start = new Position(0, 0);
    Position end = new Position(0, 8);
    Identifier identifier = new Identifier("test_name", start, end);

    LintingStrategy camelCaseIdentifier = new SnakeCaseIdentifier();
    FullReport fullReport = new FullReport();
    fullReport = camelCaseIdentifier.apply(identifier, fullReport);

    assertEquals(0, fullReport.getReports().size());
  }

  @Test
  public void identifierIsNotInSnakeCase() {
    Position start = new Position(0, 0);
    Position end = new Position(0, 8);
    Identifier identifier = new Identifier("testName", start, end);

    LintingStrategy camelCaseIdentifier = new SnakeCaseIdentifier();
    FullReport fullReport = new FullReport();
    fullReport = camelCaseIdentifier.apply(identifier, fullReport);

    assertEquals(1, fullReport.getReports().size());

    Report report = fullReport.getReports().get(0);
    assertEquals(start, report.start());
    assertEquals(end, report.end());
  }
}
