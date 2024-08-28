package linter.visitor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.root.AstNode;
import ast.root.AstNodeType;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import java.util.List;
import java.util.Map;
import linter.visitor.strategy.LintingStrategy;
import linter.visitor.strategy.identifier.CamelCaseIdentifier;
import linter.visitor.strategy.identifier.IdentifierLintingStrategy;
import org.junit.jupiter.api.Test;
import token.Position;

public class LinterVisitorNodeTest {
  private LinterVisitorV2 getLinterVisitorOnlyIdentifierLinting() {
    LintingStrategy idStrategy = new CamelCaseIdentifier();
    LintingStrategy mainIdStrategy = new IdentifierLintingStrategy(List.of(idStrategy));

    Map<AstNodeType, LintingStrategy> nodesStrategies =
        Map.of(AstNodeType.IDENTIFIER, mainIdStrategy);

    return new LinterVisitorV2(nodesStrategies);
  }

  @Test
  public void lintCallExpressionAllWrongCase() {
    LinterVisitorV2 visitor = getLinterVisitorOnlyIdentifierLinting();

    Position position = new Position(0, 0);
    Identifier methodIdentifier = new Identifier("test_name", position, position);
    List<AstNode> arguments = List.of(methodIdentifier, methodIdentifier);
    CallExpression callExpression = new CallExpression(methodIdentifier, arguments, true);

    LinterVisitorV2 newVisitor = (LinterVisitorV2) callExpression.accept(visitor);

    assertEquals(3, newVisitor.getFullReport().getReports().size());
  }

  @Test
  public void lintCallExpressionNoWrongCase() {
    LinterVisitorV2 visitor = getLinterVisitorOnlyIdentifierLinting();

    Position position = new Position(0, 0);
    Identifier methodIdentifier = new Identifier("testName", position, position);
    List<AstNode> arguments = List.of(methodIdentifier, methodIdentifier);
    CallExpression callExpression = new CallExpression(methodIdentifier, arguments, true);

    LinterVisitorV2 newVisitor = (LinterVisitorV2) callExpression.accept(visitor);

    assertEquals(0, newVisitor.getFullReport().getReports().size());
  }

  @Test
  public void lintCallExpressionOneCorrectCase() {
    LinterVisitorV2 visitor = getLinterVisitorOnlyIdentifierLinting();

    Position position = new Position(0, 0);
    Identifier methodIdentifier = new Identifier("test_name", position, position);
    List<AstNode> arguments =
        List.of(new Identifier("testName", position, position), methodIdentifier);
    CallExpression callExpression = new CallExpression(methodIdentifier, arguments, true);

    LinterVisitorV2 newVisitor = (LinterVisitorV2) callExpression.accept(visitor);

    assertEquals(2, newVisitor.getFullReport().getReports().size());
  }

  @Test
  public void lintBinaryExpressionTest() {
    LinterVisitorV2 visitor = getLinterVisitorOnlyIdentifierLinting();

    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("test_name", position, position);
    Identifier identifier2 = new Identifier("testName", position, position);
    BinaryExpression binaryExpression = new BinaryExpression(identifier, identifier2, "+");

    LinterVisitorV2 newVisitor = (LinterVisitorV2) binaryExpression.accept(visitor);

    assertEquals(1, newVisitor.getFullReport().getReports().size());
  }

  @Test
  public void lintAssignmentExpressionTest() {
    LinterVisitorV2 visitor = getLinterVisitorOnlyIdentifierLinting();

    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("test_name", position, position);
    Identifier identifier2 = new Identifier("testName", position, position);
    AssignmentExpression assignmentExpression =
        new AssignmentExpression(identifier, identifier2, "=");

    LinterVisitorV2 newVisitor = (LinterVisitorV2) assignmentExpression.accept(visitor);

    assertEquals(1, newVisitor.getFullReport().getReports().size());
  }
}
