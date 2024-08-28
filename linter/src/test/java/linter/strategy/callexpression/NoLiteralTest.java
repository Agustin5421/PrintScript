package linter.strategy.callexpression;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ast.identifier.Identifier;
import ast.literal.NumberLiteral;
import linter.rework.report.FullReport;
import linter.rework.visitor.strategy.LintingStrategy;
import linter.rework.visitor.strategy.callexpression.NoLiteralArgument;
import org.junit.jupiter.api.Test;
import token.Position;

public class NoLiteralTest {
  @Test
  public void callExpressionWithLiteralArgumentTest() {
    Position position = new Position(0, 0);
    NumberLiteral one = new NumberLiteral(1, position, position);

    LintingStrategy strategy = new NoLiteralArgument();
    FullReport fullReport = new FullReport();

    FullReport newReport = strategy.apply(one, fullReport);

    assertEquals(1, newReport.getReports().size());
  }

  @Test
  public void callExpressionWithoutLiteralArgumentTest() {
    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("methodName", position, position);

    LintingStrategy strategy = new NoLiteralArgument();
    FullReport fullReport = new FullReport();

    FullReport newReport = strategy.apply(identifier, fullReport);

    assertEquals(0, newReport.getReports().size());
  }
}
