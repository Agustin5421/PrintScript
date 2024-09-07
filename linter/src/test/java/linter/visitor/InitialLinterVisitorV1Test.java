package linter.visitor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ast.identifier.Identifier;
import ast.root.AstNodeType;
import ast.visitor.NodeVisitor;
import java.util.List;
import java.util.Map;
import linter.visitor.report.FullReport;
import linter.visitor.strategy.LintingStrategy;
import linter.visitor.strategy.StrategiesContainer;
import linter.visitor.strategy.identifier.WritingConventionStrategy;
import org.junit.jupiter.api.Test;
import token.Position;

public class InitialLinterVisitorV1Test {
  private LinterVisitorV1 getLinterVisitorV2() {
    LintingStrategy idStrategy =
        new WritingConventionStrategy("camelCase", "^[a-z]+(?:[A-Z]?[a-z0-9]+)*$");
    LintingStrategy mainIdStrategy = new StrategiesContainer(List.of(idStrategy));

    Map<AstNodeType, LintingStrategy> nodesStrategies =
        Map.of(AstNodeType.IDENTIFIER, mainIdStrategy);

    return new LinterVisitorV1(nodesStrategies);
  }

  @Test
  public void testImmutability() {
    LinterVisitorV1 visitor = getLinterVisitorV2();

    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("test_name", position, position);

    NodeVisitor newVisitor = identifier.accept(visitor);

    FullReport oldReport = visitor.getFullReport();
    FullReport report = ((LinterVisitorV1) newVisitor).getFullReport();

    assertEquals(0, oldReport.getReports().size());
    assertEquals(1, report.getReports().size());
  }

  @Test
  public void testNoViolations() {
    LinterVisitorV1 visitor = getLinterVisitorV2();

    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("testName", position, position);

    NodeVisitor newVisitor = identifier.accept(visitor);

    FullReport report = ((LinterVisitorV1) newVisitor).getFullReport();

    assertEquals(0, report.getReports().size());
  }

  @Test
  public void testMultipleViolations() {
    LinterVisitorV1 visitor = getLinterVisitorV2();

    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("test_name", position, position);

    NodeVisitor newVisitor = identifier.accept(visitor);
    NodeVisitor newVisitor2 = identifier.accept(newVisitor);

    FullReport report = ((LinterVisitorV1) newVisitor2).getFullReport();

    assertEquals(2, report.getReports().size());
  }
}
