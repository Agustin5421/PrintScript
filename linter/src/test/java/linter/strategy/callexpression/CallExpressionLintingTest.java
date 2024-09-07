package linter.strategy.callexpression;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.root.AstNodeType;
import ast.statements.CallExpression;
import ast.visitor.NodeVisitor;
import java.util.List;
import java.util.Map;
import linter.visitor.LinterVisitorV1;
import linter.visitor.strategy.LintingStrategy;
import linter.visitor.strategy.StrategiesContainer;
import linter.visitor.strategy.callexpression.ArgumentsStrategy;
import org.junit.jupiter.api.Test;
import token.Position;

public class CallExpressionLintingTest {
  private LinterVisitorV1 getLinterVisitorV2() {
    LintingStrategy strategy =
        new ArgumentsStrategy(
            List.of(
                AstNodeType.IDENTIFIER, AstNodeType.STRING_LITERAL, AstNodeType.NUMBER_LITERAL));
    LintingStrategy mainCallExpressionStrategy = new StrategiesContainer(List.of(strategy));
    Map<AstNodeType, LintingStrategy> nodesStrategies =
        Map.of(AstNodeType.CALL_EXPRESSION, mainCallExpressionStrategy);

    return new LinterVisitorV1(nodesStrategies);
  }

  @Test
  public void testCallExpressionLinting() {
    Position position = new Position(0, 0);
    NumberLiteral one = new NumberLiteral(1, position, position);
    NumberLiteral two = new NumberLiteral(2, position, position);
    BinaryExpression binaryExpression = new BinaryExpression(one, two, "+", position, position);
    Identifier identifier = new Identifier("methodName", position, position);
    CallExpression callExpression = new CallExpression(identifier, List.of(binaryExpression));

    LinterVisitorV1 visitor = getLinterVisitorV2();
    NodeVisitor newVisitor = visitor.visitCallExpression(callExpression);
    LinterVisitorV1 newLinterVisitorV1 = (LinterVisitorV1) newVisitor;

    assertEquals(1, newLinterVisitorV1.getFullReport().getReports().size());
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

    LinterVisitorV1 visitor = getLinterVisitorV2();
    NodeVisitor newVisitor = visitor.visitCallExpression(callExpression);
    LinterVisitorV1 newLinterVisitorV1 = (LinterVisitorV1) newVisitor;

    assertEquals(4, newLinterVisitorV1.getFullReport().getReports().size());
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

    LinterVisitorV1 visitor = getLinterVisitorV2();
    NodeVisitor newVisitor = visitor.visitCallExpression(callExpression);
    LinterVisitorV1 newLinterVisitorV1 = (LinterVisitorV1) newVisitor;

    assertEquals(3, newLinterVisitorV1.getFullReport().getReports().size());
  }

  private LinterVisitorV1 getLinterVisitorStrictArguments() {
    LintingStrategy strategy1 = new ArgumentsStrategy(List.of(AstNodeType.IDENTIFIER));
    LintingStrategy mainCallExpressionStrategy = new StrategiesContainer(List.of(strategy1));
    Map<AstNodeType, LintingStrategy> nodesStrategies =
        Map.of(AstNodeType.CALL_EXPRESSION, mainCallExpressionStrategy);

    return new LinterVisitorV1(nodesStrategies);
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

    LinterVisitorV1 visitor = getLinterVisitorStrictArguments();
    NodeVisitor newVisitor = visitor.visitCallExpression(callExpression);
    LinterVisitorV1 newLinterVisitorV1 = (LinterVisitorV1) newVisitor;

    assertEquals(4, newLinterVisitorV1.getFullReport().getReports().size());
  }
}
