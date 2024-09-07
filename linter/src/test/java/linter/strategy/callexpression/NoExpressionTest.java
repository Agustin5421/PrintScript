package linter.strategy.callexpression;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.NumberLiteral;
import ast.root.AstNodeType;
import ast.statements.CallExpression;
import java.util.List;
import linter.visitor.report.FullReport;
import linter.visitor.strategy.LintingStrategy;
import linter.visitor.strategy.callexpression.ArgumentsStrategy;
import org.junit.jupiter.api.Test;
import token.Position;

public class NoExpressionTest {
  @Test
  public void callExpressionWithExpressionArgumentTest() {
    Position position = new Position(0, 0);
    NumberLiteral one = new NumberLiteral(1, position, position);
    NumberLiteral two = new NumberLiteral(2, position, position);
    BinaryExpression binaryExpression = new BinaryExpression(one, two, "+", position, position);
    CallExpression callExpression =
        new CallExpression(
            new Identifier("methodName", position, position), List.of(binaryExpression));

    LintingStrategy strategy =
        new ArgumentsStrategy(
            List.of(
                AstNodeType.IDENTIFIER, AstNodeType.STRING_LITERAL, AstNodeType.NUMBER_LITERAL));
    FullReport fullReport = new FullReport();

    FullReport newReport = strategy.apply(callExpression, fullReport);

    assertEquals(1, newReport.getReports().size());
  }

  @Test
  public void callExpressionWithoutExpressionArgumentTest() {
    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("methodName", position, position);
    CallExpression callExpression = new CallExpression(identifier, List.of(identifier));

    LintingStrategy strategy =
        new ArgumentsStrategy(
            List.of(
                AstNodeType.IDENTIFIER, AstNodeType.STRING_LITERAL, AstNodeType.NUMBER_LITERAL));
    FullReport fullReport = new FullReport();

    FullReport newReport = strategy.apply(callExpression, fullReport);

    assertEquals(0, newReport.getReports().size());
  }
}
