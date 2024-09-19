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
import linter.visitor.strategy.StrategiesContainer;
import linter.visitor.strategy.identifier.WritingConventionStrategy;
import org.junit.jupiter.api.Test;
import token.Position;

public class LinterVisitorV1NodeTest {
  private LinterVisitorV1 getLinterVisitorOnlyIdentifierLinting() {
    LintingStrategy idStrategy =
        new WritingConventionStrategy("camelCase", "^[a-z]+(?:[A-Z]?[a-z0-9]+)*$");
    LintingStrategy mainIdStrategy = new StrategiesContainer(List.of(idStrategy));

    Map<AstNodeType, LintingStrategy> nodesStrategies =
        Map.of(AstNodeType.IDENTIFIER, mainIdStrategy);

    return new LinterVisitorV1(nodesStrategies);
  }

  @Test
  public void lintCallExpressionAllWrongCase() {
    LinterVisitorV1 visitor = getLinterVisitorOnlyIdentifierLinting();

    Position position = new Position(0, 0);
    Identifier methodIdentifier = new Identifier("test_name", position, position);
    List<AstNode> arguments = List.of(methodIdentifier, methodIdentifier);
    CallExpression callExpression = new CallExpression(methodIdentifier, arguments);

    LinterVisitorV1 newVisitor = (LinterVisitorV1) visitor.visit(callExpression);

    assertEquals(3, newVisitor.getFullReport().getReports().size());
  }

  @Test
  public void lintCallExpressionNoWrongCase() {
    LinterVisitorV1 visitor = getLinterVisitorOnlyIdentifierLinting();

    Position position = new Position(0, 0);
    Identifier methodIdentifier = new Identifier("testName", position, position);
    List<AstNode> arguments = List.of(methodIdentifier, methodIdentifier);
    CallExpression callExpression = new CallExpression(methodIdentifier, arguments);

    LinterVisitorV1 newVisitor = (LinterVisitorV1) visitor.visit(callExpression);

    assertEquals(0, newVisitor.getFullReport().getReports().size());
  }

  @Test
  public void lintCallExpressionOneCorrectCase() {
    LinterVisitorV1 visitor = getLinterVisitorOnlyIdentifierLinting();

    Position position = new Position(0, 0);
    Identifier methodIdentifier = new Identifier("test_name", position, position);
    List<AstNode> arguments =
        List.of(new Identifier("testName", position, position), methodIdentifier);
    CallExpression callExpression = new CallExpression(methodIdentifier, arguments);

    LinterVisitorV1 newVisitor = (LinterVisitorV1) visitor.visit(callExpression);

    assertEquals(2, newVisitor.getFullReport().getReports().size());
  }

  @Test
  public void lintBinaryExpressionTest() {
    LinterVisitorV1 visitor = getLinterVisitorOnlyIdentifierLinting();

    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("test_name", position, position);
    Identifier identifier2 = new Identifier("testName", position, position);
    BinaryExpression binaryExpression = new BinaryExpression(identifier, identifier2, "+");

    LinterVisitorV1 newVisitor = (LinterVisitorV1) visitor.visit(binaryExpression);

    assertEquals(1, newVisitor.getFullReport().getReports().size());
  }

  @Test
  public void lintAssignmentExpressionTest() {
    LinterVisitorV1 visitor = getLinterVisitorOnlyIdentifierLinting();

    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("test_name", position, position);
    Identifier identifier2 = new Identifier("testName", position, position);
    AssignmentExpression assignmentExpression =
        new AssignmentExpression(identifier, identifier2, "=");

    LinterVisitorV1 newVisitor = (LinterVisitorV1) visitor.visit(assignmentExpression);

    assertEquals(1, newVisitor.getFullReport().getReports().size());
  }
}
