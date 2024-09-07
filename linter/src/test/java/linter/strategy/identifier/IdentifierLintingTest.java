package linter.strategy.identifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ast.identifier.Identifier;
import java.util.List;
import linter.visitor.report.FullReport;
import linter.visitor.strategy.LintingStrategy;
import linter.visitor.strategy.StrategiesContainer;
import linter.visitor.strategy.identifier.WritingConventionStrategy;
import org.junit.jupiter.api.Test;
import token.Position;

public class IdentifierLintingTest {
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

    FullReport fullReport = new FullReport();
    for (Identifier identifier : identifiers) {
      fullReport = mainIdLinting.apply(identifier, fullReport);
    }

    assertEquals(1, fullReport.getReports().size());
  }

  @Test
  public void severalIdentifiersSnakeCaseTest() {
    List<Identifier> identifiers = getIdentifiers();

    LintingStrategy snakeCaseIdentifier =
        new WritingConventionStrategy("snakeCase", "^[a-z]+(_[a-z0-9]+)*$");
    LintingStrategy mainIdLinting = new StrategiesContainer(List.of(snakeCaseIdentifier));

    FullReport fullReport = new FullReport();
    for (Identifier identifier : identifiers) {
      fullReport = mainIdLinting.apply(identifier, fullReport);
    }

    assertEquals(1, fullReport.getReports().size());
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

    FullReport fullReport = new FullReport();
    for (Identifier identifier : identifiers) {
      fullReport = mainIdLinting.apply(identifier, fullReport);
    }

    assertEquals(2, fullReport.getReports().size());
  }
}
