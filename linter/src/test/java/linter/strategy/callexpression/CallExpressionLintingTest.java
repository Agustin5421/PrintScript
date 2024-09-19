package linter.strategy.callexpression;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.root.AstNodeType;
import ast.statements.CallExpression;
import java.util.List;
import java.util.Map;
import linter.visitor.ReworkedLinterVisitor;
import linter.visitor.strategy.LintingStrategy;
import linter.visitor.strategy.NewLinterVisitor;
import linter.visitor.strategy.StrategiesContainer;
import linter.visitor.strategy.callexpression.ArgumentsStrategy;
import linter.visitor.strategy.callexpression.CallExpressionTraversing;
import org.junit.jupiter.api.Test;
import output.OutputListString;
import strategy.StrategyContainer;
import token.Position;

public class CallExpressionLintingTest {
  private NewLinterVisitor getLinterVisitorV2() {
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
            AstNodeType.STRING_LITERAL, mock,
            AstNodeType.BINARY_EXPRESSION, mock);

    StrategyContainer<AstNodeType, LintingStrategy> mockStrategy =
        new StrategyContainer<>(nodesStrategies, "Can't lint: ");

    return new ReworkedLinterVisitor(mockStrategy, new OutputListString());
  }

  @Test
  public void testCallExpressionLinting() {
    Position position = new Position(0, 0);
    NumberLiteral one = new NumberLiteral(1, position, position);
    NumberLiteral two = new NumberLiteral(2, position, position);
    BinaryExpression binaryExpression = new BinaryExpression(one, two, "+", position, position);
    Identifier identifier = new Identifier("methodName", position, position);
    CallExpression callExpression = new CallExpression(identifier, List.of(binaryExpression));

    NewLinterVisitor visitor = getLinterVisitorV2();
    NewLinterVisitor newVisitor = visitor.lintNode(callExpression);
    OutputListString output = (OutputListString) newVisitor.getOutput();

    assertEquals(1, output.getSavedResults().size());
  }

  @Test
  public void testCallExpressionLintingSeveralExpressions() {
    Position position = new Position(0, 0);
    NumberLiteral one = new NumberLiteral(1, position, position);
    NumberLiteral two = new NumberLiteral(2, position, position);
    BinaryExpression binaryExpression = new BinaryExpression(one, two, "+", position, position);
    Identifier identifier = new Identifier("methodName", position, position);
    CallExpression callExpression =
        new CallExpression(
            identifier,
            List.of(binaryExpression, binaryExpression, binaryExpression, binaryExpression));

    NewLinterVisitor visitor = getLinterVisitorV2();
    NewLinterVisitor newVisitor = visitor.lintNode(callExpression);
    OutputListString output = (OutputListString) newVisitor.getOutput();

    assertEquals(4, output.getSavedResults().size());
  }

  @Test
  public void testCallExpressionLintingMixedArguments() {
    Position position = new Position(0, 0);
    NumberLiteral one = new NumberLiteral(1, position, position);
    NumberLiteral two = new NumberLiteral(2, position, position);
    BinaryExpression binaryExpression = new BinaryExpression(one, two, "+", position, position);
    Identifier identifier = new Identifier("methodName", position, position);
    CallExpression callExpression =
        new CallExpression(
            identifier,
            List.of(binaryExpression, identifier, binaryExpression, binaryExpression, identifier));

    NewLinterVisitor visitor = getLinterVisitorV2();
    NewLinterVisitor newVisitor = visitor.lintNode(callExpression);
    OutputListString output = (OutputListString) newVisitor.getOutput();

    assertEquals(3, output.getSavedResults().size());
  }

  private NewLinterVisitor getLinterVisitorStrictArguments() {
    LintingStrategy strategy1 = new ArgumentsStrategy(List.of(AstNodeType.IDENTIFIER));
    LintingStrategy mainCallExpressionStrategy = new StrategiesContainer(List.of(strategy1));
    Map<AstNodeType, LintingStrategy> nodesStrategies =
        Map.of(AstNodeType.CALL_EXPRESSION, mainCallExpressionStrategy);

    StrategyContainer<AstNodeType, LintingStrategy> mockStrategy =
        new StrategyContainer<>(nodesStrategies, "Can't lint: ");

    return new ReworkedLinterVisitor(mockStrategy, new OutputListString());
  }

  @Test
  public void testCallExpressionLintingStrictArguments() {
    Position position = new Position(0, 0);
    NumberLiteral one = new NumberLiteral(1, position, position);
    StringLiteral two = new StringLiteral("hi", position, position);
    BinaryExpression binaryExpression = new BinaryExpression(one, two, "+", position, position);
    Identifier identifier = new Identifier("methodName", position, position);
    CallExpression callExpression =
        new CallExpression(
            identifier, List.of(binaryExpression, one, two, binaryExpression, identifier));

    NewLinterVisitor visitor = getLinterVisitorStrictArguments();
    NewLinterVisitor newVisitor = visitor.lintNode(callExpression);
    OutputListString output = (OutputListString) newVisitor.getOutput();

    assertEquals(4, output.getSavedResults().size());
  }
}
