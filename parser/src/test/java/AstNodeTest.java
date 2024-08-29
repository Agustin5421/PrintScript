import static org.junit.jupiter.api.Assertions.assertEquals;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.root.AstNode;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.VariableDeclaration;
import java.util.List;
import org.junit.jupiter.api.Test;
import token.Position;

public class AstNodeTest {
  private final Position defaultPosition = new Position(0, 0);
  private final Identifier identifier = new Identifier("myVar", defaultPosition, defaultPosition);

  @Test
  public void astNodeTest1() {
    BinaryExpression binaryExpression = new BinaryExpression(identifier, identifier, "+");
    assertEquals(defaultPosition, binaryExpression.start());
    assertEquals(defaultPosition, binaryExpression.end());
  }

  @Test
  public void astNodeTest2() {
    AssignmentExpression assignmentExpression =
        new AssignmentExpression(identifier, identifier, "=");
    assertEquals(defaultPosition, assignmentExpression.start());
    assertEquals(defaultPosition, assignmentExpression.end());
  }

  @Test
  public void astNodeTest3() {
    List<AstNode> arguments = List.of(identifier);
    CallExpression callExpression = new CallExpression(identifier, arguments, false);
    assertEquals(defaultPosition, callExpression.start());
    assertEquals(defaultPosition, callExpression.end());
  }

  @Test
  public void astNodeTest4() {
    VariableDeclaration variableDeclaration = new VariableDeclaration(identifier, identifier);
    assertEquals(defaultPosition, variableDeclaration.start());
    assertEquals(defaultPosition, variableDeclaration.end());
  }
}
