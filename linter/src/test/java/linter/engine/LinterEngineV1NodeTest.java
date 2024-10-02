package linter.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.root.AstNode;
import ast.root.AstNodeType;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import java.util.List;
import java.util.Map;
import linter.engine.strategy.LintingStrategy;
import linter.engine.strategy.StrategiesContainer;
import linter.engine.strategy.assign.AssignmentExpressionTraversing;
import linter.engine.strategy.binary.BinaryExpressionTraversing;
import linter.engine.strategy.callexpression.CallExpressionTraversing;
import linter.engine.strategy.identifier.WritingConventionStrategy;
import org.junit.jupiter.api.Test;
import output.OutputListString;
import position.Position;
import strategy.StrategyContainer;

public class LinterEngineV1NodeTest {
  private LinterEngine getLinterEngineOnlyIdentifierLinting() {
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

    return new LinterEngine(mockStrategy, new OutputListString());
  }

  @Test
  public void lintCallExpressionAllWrongCase() {
    LinterEngine engine = getLinterEngineOnlyIdentifierLinting();

    Position position = new Position(0, 0);
    Identifier methodIdentifier = new Identifier("test_name", position, position);
    List<AstNode> arguments = List.of(methodIdentifier, methodIdentifier);
    CallExpression callExpression = new CallExpression(methodIdentifier, arguments);

    LinterEngine newEngine = engine.lintNode(callExpression);

    OutputListString output = (OutputListString) newEngine.getOutput();
    assertEquals(3, output.getSavedResults().size());
  }

  @Test
  public void lintCallExpressionNoWrongCase() {
    LinterEngine engine = getLinterEngineOnlyIdentifierLinting();

    Position position = new Position(0, 0);
    Identifier methodIdentifier = new Identifier("testName", position, position);
    List<AstNode> arguments = List.of(methodIdentifier, methodIdentifier);
    CallExpression callExpression = new CallExpression(methodIdentifier, arguments);

    LinterEngine newEngine = engine.lintNode(callExpression);

    OutputListString output = (OutputListString) newEngine.getOutput();
    assertEquals(0, output.getSavedResults().size());
  }

  @Test
  public void lintCallExpressionOneCorrectCase() {
    LinterEngine engine = getLinterEngineOnlyIdentifierLinting();

    Position position = new Position(0, 0);
    Identifier methodIdentifier = new Identifier("test_name", position, position);
    List<AstNode> arguments =
        List.of(new Identifier("testName", position, position), methodIdentifier);
    CallExpression callExpression = new CallExpression(methodIdentifier, arguments);

    LinterEngine newEngine = engine.lintNode(callExpression);

    OutputListString output = (OutputListString) newEngine.getOutput();
    assertEquals(2, output.getSavedResults().size());
  }

  @Test
  public void lintBinaryExpressionTest() {
    LinterEngine engine = getLinterEngineOnlyIdentifierLinting();

    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("test_name", position, position);
    Identifier identifier2 = new Identifier("testName", position, position);
    BinaryExpression binaryExpression = new BinaryExpression(identifier, identifier2, "+");

    LinterEngine newEngine = engine.lintNode(binaryExpression);

    OutputListString output = (OutputListString) newEngine.getOutput();
    assertEquals(1, output.getSavedResults().size());
  }

  @Test
  public void lintAssignmentExpressionTest() {
    LinterEngine engine = getLinterEngineOnlyIdentifierLinting();

    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("test_name", position, position);
    Identifier identifier2 = new Identifier("testName", position, position);
    AssignmentExpression assignmentExpression =
        new AssignmentExpression(identifier, identifier2, "=");

    LinterEngine newEngine = engine.lintNode(assignmentExpression);

    OutputListString output = (OutputListString) newEngine.getOutput();
    assertEquals(1, output.getSavedResults().size());
  }
}
