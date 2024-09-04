package linter.strategy.identifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ast.identifier.Identifier;
import linter.report.FullReport;
import linter.report.Report;
import linter.visitor.strategy.LintingStrategy;
import linter.visitor.strategy.identifier.CamelCaseIdentifier;
import org.junit.jupiter.api.Test;
import token.Position;

public class CamelCaseTest {
  @Test
  public void identifierIsInCamelCase() {
    Position start = new Position(0, 0);
    Position end = new Position(0, 8);
    Identifier identifier = new Identifier("testName", start, end);

    LintingStrategy camelCaseIdentifier = new CamelCaseIdentifier();
    FullReport fullReport = new FullReport();
    fullReport = camelCaseIdentifier.apply(identifier, fullReport);

    assertEquals(0, fullReport.getReports().size());
  }

  @Test
  public void identifierIsNotInCamelCase() {
    Position start = new Position(0, 0);
    Position end = new Position(0, 8);
    Identifier identifier = new Identifier("test_name", start, end);

    LintingStrategy camelCaseIdentifier = new CamelCaseIdentifier();
    FullReport fullReport = new FullReport();
    fullReport = camelCaseIdentifier.apply(identifier, fullReport);

    assertEquals(1, fullReport.getReports().size());

    Report report = fullReport.getReports().get(0);
    assertEquals(start, report.start());
    assertEquals(end, report.end());
  }
}