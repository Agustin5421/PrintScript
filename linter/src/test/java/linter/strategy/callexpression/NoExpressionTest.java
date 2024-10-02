package linter.strategy.callexpression;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.NumberLiteral;
import ast.root.AstNodeType;
import ast.statements.CallExpression;
import java.util.List;
import java.util.Map;
import linter.engine.LinterEngine;
import linter.engine.strategy.LintingStrategy;
import linter.engine.strategy.StrategiesContainer;
import linter.engine.strategy.callexpression.ArgumentsStrategy;
import linter.engine.strategy.callexpression.CallExpressionTraversing;
import org.junit.jupiter.api.Test;
import output.OutputReport;
import position.Position;
import report.FullReport;
import strategy.StrategyContainer;

public class NoExpressionTest {
  private LinterEngine getLinterEngine() {
    LintingStrategy strategy =
        new ArgumentsStrategy(
            List.of(
                AstNodeType.IDENTIFIER, AstNodeType.STRING_LITERAL, AstNodeType.NUMBER_LITERAL));
    LintingStrategy mainCallExpressionStrategy = new StrategiesContainer(List.of(strategy));

    StrategiesContainer mock = new StrategiesContainer(List.of());
    Map<AstNodeType, LintingStrategy> nodesStrategies =
        Map.of(
            AstNodeType.CALL_EXPRESSION, new CallExpressionTraversing(mainCallExpressionStrategy),
            AstNodeType.IDENTIFIER, mock,
            AstNodeType.NUMBER_LITERAL, mock,
            AstNodeType.BINARY_EXPRESSION, mock);

    StrategyContainer<AstNodeType, LintingStrategy> mockStrategy =
        new StrategyContainer<>(nodesStrategies, "Can't lint: ");

    return new LinterEngine(mockStrategy, new OutputReport());
  }

  @Test
  public void callExpressionWithExpressionArgumentTest() {
    Position position = new Position(0, 0);
    NumberLiteral one = new NumberLiteral(1, position, position);
    NumberLiteral two = new NumberLiteral(2, position, position);
    BinaryExpression binaryExpression = new BinaryExpression(one, two, "+", position, position);
    CallExpression callExpression =
        new CallExpression(
            new Identifier("methodName", position, position), List.of(binaryExpression));

    LinterEngine engine = getLinterEngine();
    engine.lintNode(callExpression);

    OutputReport output = (OutputReport) engine.getOutput();

    assertEquals(1, output.getFullReport().getReports().size());
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

    FullReport newReport = strategy.oldApply(callExpression, fullReport);

    assertEquals(0, newReport.getReports().size());
  }
}
