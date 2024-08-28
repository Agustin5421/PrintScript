package linter.strategy.identifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ast.identifier.Identifier;
import java.util.List;
import linter.report.FullReport;
import linter.visitor.strategy.LintingStrategy;
import linter.visitor.strategy.identifier.CamelCaseIdentifier;
import linter.visitor.strategy.identifier.IdentifierLintingStrategy;
import linter.visitor.strategy.identifier.SnakeCaseIdentifier;
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

    LintingStrategy camelCaseIdentifier = new CamelCaseIdentifier();
    LintingStrategy mainIdLinting = new IdentifierLintingStrategy(List.of(camelCaseIdentifier));

    FullReport fullReport = new FullReport();
    for (Identifier identifier : identifiers) {
      fullReport = mainIdLinting.apply(identifier, fullReport);
    }

    assertEquals(1, fullReport.getReports().size());
  }

  @Test
  public void severalIdentifiersSnakeCaseTest() {
    List<Identifier> identifiers = getIdentifiers();

    LintingStrategy snakeCaseIdentifier = new SnakeCaseIdentifier();
    LintingStrategy mainIdLinting = new IdentifierLintingStrategy(List.of(snakeCaseIdentifier));

    FullReport fullReport = new FullReport();
    for (Identifier identifier : identifiers) {
      fullReport = mainIdLinting.apply(identifier, fullReport);
    }

    assertEquals(1, fullReport.getReports().size());
  }

  @Test
  public void severalIdentifiersBothCaseTest() {
    List<Identifier> identifiers = getIdentifiers();

    LintingStrategy camelCaseIdentifier = new CamelCaseIdentifier();
    LintingStrategy snakeCaseIdentifier = new SnakeCaseIdentifier();
    LintingStrategy mainIdLinting =
        new IdentifierLintingStrategy(List.of(camelCaseIdentifier, snakeCaseIdentifier));

    FullReport fullReport = new FullReport();
    for (Identifier identifier : identifiers) {
      fullReport = mainIdLinting.apply(identifier, fullReport);
    }

    assertEquals(2, fullReport.getReports().size());
  }
}
