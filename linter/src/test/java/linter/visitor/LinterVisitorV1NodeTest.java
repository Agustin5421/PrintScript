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
import linter.visitor.strategy.NewLinterVisitor;
import linter.visitor.strategy.StrategiesContainer;
import linter.visitor.strategy.assign.AssignmentExpressionTraversing;
import linter.visitor.strategy.binary.BinaryExpressionTraversing;
import linter.visitor.strategy.callexpression.CallExpressionTraversing;
import linter.visitor.strategy.identifier.WritingConventionStrategy;
import org.junit.jupiter.api.Test;
import output.OutputListString;
import strategy.StrategyContainer;
import token.Position;

public class LinterVisitorV1NodeTest {
  private NewLinterVisitor getLinterVisitorOnlyIdentifierLinting() {
    LintingStrategy idStrategy =
        new WritingConventionStrategy("camelCase", "^[a-z]+(?:[A-Z]?[a-z0-9]+)*$");
    LintingStrategy mainIdStrategy = new StrategiesContainer(List.of(idStrategy));

    Map<AstNodeType, LintingStrategy> nodesStrategies =
        Map.of(
            AstNodeType.IDENTIFIER, mainIdStrategy,
            AstNodeType.CALL_EXPRESSION,
                new CallExpressionTraversing(new StrategiesContainer(List.of())),
            AstNodeType.BINARY_EXPRESSION,
                new BinaryExpressionTraversing(new StrategiesContainer(List.of())),
            AstNodeType.ASSIGNMENT_EXPRESSION,
                new AssignmentExpressionTraversing(new StrategiesContainer(List.of())));

    StrategyContainer<AstNodeType, LintingStrategy> mockStrategy =
        new StrategyContainer<>(nodesStrategies, "Can't lint: ");

    return new ReworkedLinterVisitor(mockStrategy, new OutputListString());
  }

  @Test
  public void lintCallExpressionAllWrongCase() {
    NewLinterVisitor visitor = getLinterVisitorOnlyIdentifierLinting();

    Position position = new Position(0, 0);
    Identifier methodIdentifier = new Identifier("test_name", position, position);
    List<AstNode> arguments = List.of(methodIdentifier, methodIdentifier);
    CallExpression callExpression = new CallExpression(methodIdentifier, arguments);

    NewLinterVisitor newVisitor = visitor.lintNode(callExpression);

    OutputListString output = (OutputListString) newVisitor.getOutput();
    assertEquals(3, output.getSavedResults().size());
  }

  @Test
  public void lintCallExpressionNoWrongCase() {
    NewLinterVisitor visitor = getLinterVisitorOnlyIdentifierLinting();

    Position position = new Position(0, 0);
    Identifier methodIdentifier = new Identifier("testName", position, position);
    List<AstNode> arguments = List.of(methodIdentifier, methodIdentifier);
    CallExpression callExpression = new CallExpression(methodIdentifier, arguments);

    NewLinterVisitor newVisitor = visitor.lintNode(callExpression);

    OutputListString output = (OutputListString) newVisitor.getOutput();
    assertEquals(0, output.getSavedResults().size());
  }

  @Test
  public void lintCallExpressionOneCorrectCase() {
    NewLinterVisitor visitor = getLinterVisitorOnlyIdentifierLinting();

    Position position = new Position(0, 0);
    Identifier methodIdentifier = new Identifier("test_name", position, position);
    List<AstNode> arguments =
        List.of(new Identifier("testName", position, position), methodIdentifier);
    CallExpression callExpression = new CallExpression(methodIdentifier, arguments);

    NewLinterVisitor newVisitor = visitor.lintNode(callExpression);

    OutputListString output = (OutputListString) newVisitor.getOutput();
    assertEquals(2, output.getSavedResults().size());
  }

  @Test
  public void lintBinaryExpressionTest() {
    NewLinterVisitor visitor = getLinterVisitorOnlyIdentifierLinting();

    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("test_name", position, position);
    Identifier identifier2 = new Identifier("testName", position, position);
    BinaryExpression binaryExpression = new BinaryExpression(identifier, identifier2, "+");

    NewLinterVisitor newVisitor = visitor.lintNode(binaryExpression);

    OutputListString output = (OutputListString) newVisitor.getOutput();
    assertEquals(1, output.getSavedResults().size());
  }

  @Test
  public void lintAssignmentExpressionTest() {
    NewLinterVisitor visitor = getLinterVisitorOnlyIdentifierLinting();

    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("test_name", position, position);
    Identifier identifier2 = new Identifier("testName", position, position);
    AssignmentExpression assignmentExpression =
        new AssignmentExpression(identifier, identifier2, "=");

    NewLinterVisitor newVisitor = visitor.lintNode(assignmentExpression);

    OutputListString output = (OutputListString) newVisitor.getOutput();
    assertEquals(1, output.getSavedResults().size());
  }
}
