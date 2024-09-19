package linter.strategy.identifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ast.identifier.Identifier;
import linter.visitor.report.FullReport;
import linter.visitor.report.Report;
import linter.visitor.strategy.LintingStrategy;
import linter.visitor.strategy.identifier.WritingConventionStrategy;
import org.junit.jupiter.api.Test;
import token.Position;

public class WritingConventionTest {
  private LintingStrategy getCamelCaseLintingStrategy() {
    String writingConventionName = "camelCase";
    String writingConventionPattern = "^[a-z]+(?:[A-Z]?[a-z0-9]+)*$";
    return new WritingConventionStrategy(writingConventionName, writingConventionPattern);
  }

  private LintingStrategy getSnakeCaseLintingStrategy() {
    String writingConventionName = "snakeCase";
    String writingConventionPattern = "^[a-z]+(_[a-z0-9]+)*$";
    return new WritingConventionStrategy(writingConventionName, writingConventionPattern);
  }

  private Identifier getCamelCaseIdentifier() {
    Position start = new Position(0, 0);
    Position end = new Position(0, 8);
    return new Identifier("testName", start, end);
  }

  private Identifier getSnakeCaseIdentifier() {
    Position start = new Position(0, 0);
    Position end = new Position(0, 8);
    return new Identifier("test_name", start, end);
  }

  @Test
  public void identifierIsInCamelCase() {
    Identifier identifier = getCamelCaseIdentifier();

    LintingStrategy camelCaseIdentifier = getCamelCaseLintingStrategy();
    FullReport fullReport = new FullReport();

    fullReport = camelCaseIdentifier.oldApply(identifier, fullReport);

    assertEquals(0, fullReport.getReports().size());
  }

  @Test
  public void identifierIsNotInCamelCase() {
    Identifier identifier = getSnakeCaseIdentifier();

    LintingStrategy camelCaseIdentifier = getCamelCaseLintingStrategy();
    FullReport fullReport = new FullReport();

    fullReport = camelCaseIdentifier.oldApply(identifier, fullReport);

    assertEquals(1, fullReport.getReports().size());

    Report report = fullReport.getReports().get(0);
    assertEquals(identifier.start(), report.start());
    assertEquals(identifier.end(), report.end());
  }

  @Test
  public void identifierIsInSnakeCase() {
    Identifier identifier = getSnakeCaseIdentifier();

    LintingStrategy camelCaseIdentifier = getSnakeCaseLintingStrategy();
    FullReport fullReport = new FullReport();

    fullReport = camelCaseIdentifier.oldApply(identifier, fullReport);

    assertEquals(0, fullReport.getReports().size());
  }

  @Test
  public void identifierIsNotInSnakeCase() {
    Identifier identifier = getCamelCaseIdentifier();

    LintingStrategy camelCaseIdentifier = getSnakeCaseLintingStrategy();
    FullReport fullReport = new FullReport();

    fullReport = camelCaseIdentifier.oldApply(identifier, fullReport);

    assertEquals(1, fullReport.getReports().size());

    Report report = fullReport.getReports().get(0);
    assertEquals(identifier.start(), report.start());
    assertEquals(identifier.end(), report.end());
  }
}
