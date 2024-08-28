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
import linter.visitor.LinterVisitorV2;
import linter.visitor.strategy.LintingStrategy;
import linter.visitor.strategy.callexpression.CallExpressionLintingStrategy;
import linter.visitor.strategy.callexpression.NoExpressionArgument;
import linter.visitor.strategy.callexpression.NoLiteralArgument;
import org.junit.jupiter.api.Test;
import token.Position;

public class CallExpressionLintingTest {
  private LinterVisitorV2 getLinterVisitorV2() {
    LintingStrategy strategy = new NoExpressionArgument();
    LintingStrategy mainCallExpressionStrategy =
        new CallExpressionLintingStrategy(List.of(strategy));
    Map<AstNodeType, LintingStrategy> nodesStrategies =
        Map.of(AstNodeType.CALL_EXPRESSION, mainCallExpressionStrategy);

    return new LinterVisitorV2(nodesStrategies);
  }

  @Test
  public void testCallExpressionLinting() {
    Position position = new Position(0, 0);
    NumberLiteral one = new NumberLiteral(1, position, position);
    NumberLiteral two = new NumberLiteral(2, position, position);
    BinaryExpression binaryExpression = new BinaryExpression(one, two, "+", position, position);
    Identifier identifier = new Identifier("methodName", position, position);
    CallExpression callExpression = new CallExpression(identifier, List.of(binaryExpression), true);

    LinterVisitorV2 visitor = getLinterVisitorV2();
    NodeVisitor newVisitor = visitor.visitCallExpression(callExpression);
    LinterVisitorV2 newLinterVisitor = (LinterVisitorV2) newVisitor;

    assertEquals(1, newLinterVisitor.getFullReport().getReports().size());
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
            List.of(binaryExpression, binaryExpression, binaryExpression, binaryExpression),
            true);

    LinterVisitorV2 visitor = getLinterVisitorV2();
    NodeVisitor newVisitor = visitor.visitCallExpression(callExpression);
    LinterVisitorV2 newLinterVisitor = (LinterVisitorV2) newVisitor;

    assertEquals(4, newLinterVisitor.getFullReport().getReports().size());
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
            List.of(binaryExpression, identifier, binaryExpression, binaryExpression, identifier),
            true);

    LinterVisitorV2 visitor = getLinterVisitorV2();
    NodeVisitor newVisitor = visitor.visitCallExpression(callExpression);
    LinterVisitorV2 newLinterVisitor = (LinterVisitorV2) newVisitor;

    assertEquals(3, newLinterVisitor.getFullReport().getReports().size());
  }

  private LinterVisitorV2 getLinterVisitorStrictArguments() {
    LintingStrategy strategy1 = new NoExpressionArgument();
    LintingStrategy strategy2 = new NoLiteralArgument();
    LintingStrategy mainCallExpressionStrategy =
        new CallExpressionLintingStrategy(List.of(strategy1, strategy2));
    Map<AstNodeType, LintingStrategy> nodesStrategies =
        Map.of(AstNodeType.CALL_EXPRESSION, mainCallExpressionStrategy);

    return new LinterVisitorV2(nodesStrategies);
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
            identifier, List.of(binaryExpression, one, two, binaryExpression, identifier), true);

    LinterVisitorV2 visitor = getLinterVisitorStrictArguments();
    NodeVisitor newVisitor = visitor.visitCallExpression(callExpression);
    LinterVisitorV2 newLinterVisitor = (LinterVisitorV2) newVisitor;

    assertEquals(4, newLinterVisitor.getFullReport().getReports().size());
  }
}
