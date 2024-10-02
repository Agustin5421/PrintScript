package linter.strategy.callexpression;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ast.identifier.Identifier;
import ast.literal.NumberLiteral;
import ast.root.AstNodeType;
import ast.statements.CallExpression;
import java.util.List;
import linter.engine.strategy.LintingStrategy;
import linter.engine.strategy.callexpression.ArgumentsStrategy;
import org.junit.jupiter.api.Test;
import position.Position;
import report.FullReport;

public class NoLiteralTest {
  @Test
  public void callExpressionWithLiteralArgumentTest() {
    Position position = new Position(0, 0);
    NumberLiteral one = new NumberLiteral(1, position, position);
    CallExpression callExpression =
        new CallExpression(new Identifier("methodName", position, position), List.of(one));

    LintingStrategy strategy =
        new ArgumentsStrategy(
            List.of(
                AstNodeType.IDENTIFIER,
                AstNodeType.CALL_EXPRESSION,
                AstNodeType.BINARY_EXPRESSION,
                AstNodeType.ASSIGNMENT_EXPRESSION));
    FullReport fullReport = new FullReport();

    FullReport newReport = strategy.oldApply(callExpression, fullReport);

    assertEquals(1, newReport.getReports().size());
  }

  @Test
  public void callExpressionWithoutLiteralArgumentTest() {
    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("methodName", position, position);
    CallExpression callExpression = new CallExpression(identifier, List.of(identifier));

    LintingStrategy strategy =
        new ArgumentsStrategy(
            List.of(
                AstNodeType.IDENTIFIER,
                AstNodeType.CALL_EXPRESSION,
                AstNodeType.BINARY_EXPRESSION,
                AstNodeType.ASSIGNMENT_EXPRESSION));
    FullReport fullReport = new FullReport();

    FullReport newReport = strategy.oldApply(callExpression, fullReport);

    assertEquals(0, newReport.getReports().size());
  }
}
