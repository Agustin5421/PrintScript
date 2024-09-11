package interpreter;

import static org.junit.Assert.assertEquals;

import ast.identifier.Identifier;
import org.junit.jupiter.api.Test;
import token.Position;

public class InterpreterVisitorTest {
  private final Position defaultPosition = new Position(0, 0);

  @Test
  public void testAssignmentExpression() {
    String code = "let name: string = \"value\"; name = \"otherValue\";";
    Interpreter interpreter = new Interpreter("1.0");
    VariablesRepository repository = interpreter.executeProgram(code);
    assertEquals(
        "\"otherValue\"",
        repository.getVariable(new Identifier("name", defaultPosition, defaultPosition)).value());
  }

  @Test
  public void testSimpleNodes() {
    String code = "let name: string = \"value\"; let number: number = 42;";
    Interpreter interpreter = new Interpreter("1.0");
    VariablesRepository repository = interpreter.executeProgram(code);
    assertEquals(
        "\"value\"",
        repository.getVariable(new Identifier("name", defaultPosition, defaultPosition)).value());
    assertEquals(
        42,
        repository.getVariable(new Identifier("number", defaultPosition, defaultPosition)).value());
  }
}
