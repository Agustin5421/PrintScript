package linter.strategy.callexpression;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import strategy.StrategyContainer;

public class NoLiteralTest {
  private LinterEngine getLinterEngine() {
    LintingStrategy strategy =
        new ArgumentsStrategy(
            List.of(
                AstNodeType.IDENTIFIER,
                AstNodeType.CALL_EXPRESSION,
                AstNodeType.BINARY_EXPRESSION,
                AstNodeType.ASSIGNMENT_EXPRESSION));
    LintingStrategy mainCallExpressionStrategy = new StrategiesContainer(List.of(strategy));

    StrategiesContainer mock = new StrategiesContainer(List.of());
    Map<AstNodeType, LintingStrategy> nodesStrategies =
        Map.of(
            AstNodeType.CALL_EXPRESSION, new CallExpressionTraversing(mainCallExpressionStrategy),
            AstNodeType.IDENTIFIER, mock,
            AstNodeType.NUMBER_LITERAL, mock);

    StrategyContainer<AstNodeType, LintingStrategy> mockStrategy =
        new StrategyContainer<>(nodesStrategies, "Can't lint: ");

    return new LinterEngine(mockStrategy, new OutputReport());
  }

  @Test
  public void callExpressionWithLiteralArgumentTest() {
    Position position = new Position(0, 0);
    NumberLiteral one = new NumberLiteral(1, position, position);
    CallExpression callExpression =
        new CallExpression(new Identifier("methodName", position, position), List.of(one));

    LinterEngine engine = getLinterEngine();
    engine.lintNode(callExpression);

    OutputReport output = (OutputReport) engine.getOutput();

    assertEquals(1, output.getFullReport().getReports().size());
  }

  @Test
  public void callExpressionWithoutLiteralArgumentTest() {
    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("methodName", position, position);
    CallExpression callExpression = new CallExpression(identifier, List.of(identifier));

    LinterEngine engine = getLinterEngine();
    engine.lintNode(callExpression);

    OutputReport output = (OutputReport) engine.getOutput();

    assertEquals(0, output.getFullReport().getReports().size());
  }
}
