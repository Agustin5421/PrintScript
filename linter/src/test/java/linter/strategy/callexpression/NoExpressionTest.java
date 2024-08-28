package linter.strategy.callexpression;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.NumberLiteral;
import linter.report.FullReport;
import linter.visitor.strategy.LintingStrategy;
import linter.visitor.strategy.callexpression.NoExpressionArgument;
import org.junit.jupiter.api.Test;
import token.Position;

public class NoExpressionTest {
  @Test
  public void callExpressionWithExpressionArgumentTest() {
    Position position = new Position(0, 0);
    NumberLiteral one = new NumberLiteral(1, position, position);
    NumberLiteral two = new NumberLiteral(2, position, position);
    BinaryExpression binaryExpression = new BinaryExpression(one, two, "+", position, position);

    LintingStrategy strategy = new NoExpressionArgument();
    FullReport fullReport = new FullReport();

    FullReport newReport = strategy.apply(binaryExpression, fullReport);

    assertEquals(1, newReport.getReports().size());
  }

  @Test
  public void callExpressionWithoutExpressionArgumentTest() {
    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("methodName", position, position);

    LintingStrategy strategy = new NoExpressionArgument();
    FullReport fullReport = new FullReport();

    FullReport newReport = strategy.apply(identifier, fullReport);

    assertEquals(0, newReport.getReports().size());
  }
}
