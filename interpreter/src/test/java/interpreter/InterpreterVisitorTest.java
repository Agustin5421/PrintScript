package interpreter;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.root.AstNode;
import ast.root.Program;
import ast.statements.AssignmentExpression;
import ast.statements.VariableDeclaration;
import java.util.List;
import org.junit.jupiter.api.Test;
import token.Position;

public class InterpreterVisitorTest {
  @Test
  public void testAssignmentExpression() {
    Position defaultPosition = new Position(0, 0);
    Identifier name = new Identifier("name", defaultPosition, defaultPosition);
    StringLiteral value = new StringLiteral("value", defaultPosition, defaultPosition);
    VariableDeclaration variableDeclaration = new VariableDeclaration(name, value);

    StringLiteral otherValue = new StringLiteral("otherValue", defaultPosition, defaultPosition);
    AssignmentExpression assignmentExpression = new AssignmentExpression(name, otherValue, "=");

    List<AstNode> statements = List.of(variableDeclaration, assignmentExpression);
    Program program = new Program(statements);

    Interpreter interpreter = new Interpreter();
    interpreter.executeProgram(program);
  }

  @Test
  public void testSimpleNodes() {
    Position defaultPosition = new Position(0, 0);
    Identifier name = new Identifier("name", defaultPosition, defaultPosition);
    StringLiteral value = new StringLiteral("value", defaultPosition, defaultPosition);
    NumberLiteral number = new NumberLiteral(1, defaultPosition, defaultPosition);

    List<AstNode> statements = List.of(name, value, number);
    Program program = new Program(statements);

    Interpreter interpreter = new Interpreter();
    interpreter.executeProgram(program);
  }

  @Test
  public void testBinaryExpression() {
    Position defaultPosition = new Position(0, 0);
    StringLiteral value = new StringLiteral("value", defaultPosition, defaultPosition);
    NumberLiteral number = new NumberLiteral(1, defaultPosition, defaultPosition);
    BinaryExpression binaryExpression = new BinaryExpression(value, number, "+");

    List<AstNode> statements = List.of(binaryExpression);
    Program program = new Program(statements);

    Interpreter interpreter = new Interpreter();
    interpreter.executeProgram(program);
  }
}
