package interpreter;

import static org.junit.Assert.assertEquals;

import interpreter.visitor.repository.VariableIdentifier;
import interpreter.visitor.repository.VariablesRepository;
import org.junit.jupiter.api.Test;
import token.Position;

public class InterpreterVisitorTest {
  private final Position defaultPosition = new Position(0, 0);

  @Test
  public void testAssignmentExpression() {
    String code = "let name: string = \"value\"; name = \"otherValue\";";
    Interpreter interpreter = new Interpreter("1.0");
    VariablesRepository repository = interpreter.executeProgram(code);
    assertEquals("otherValue", repository.getNewVariable(new VariableIdentifier("name")).value());
  }

  @Test
  public void testSimpleNodes() {
    String code = "let name: string = \"value\"; let number: number = 42;";
    Interpreter interpreter = new Interpreter("1.0");
    VariablesRepository repository = interpreter.executeProgram(code);
    assertEquals("value", repository.getNewVariable(new VariableIdentifier("name")).value());
    assertEquals(42, repository.getNewVariable(new VariableIdentifier("number")).value());
  }

  @Test
  public void testInitializedVar() {
    String code = "let pi: number;\n" + "pi = 3.14;\n" + "println(pi / 2);";
    Interpreter interpreter = new Interpreter("1.0");
    VariablesRepository repository = interpreter.executeProgram(code);
    assertEquals(3.14, repository.getNewVariable(new VariableIdentifier("pi")).value());
  }
}
