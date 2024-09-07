package interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ast.identifier.Identifier;
import java.io.ByteArrayInputStream;
import org.junit.jupiter.api.Test;
import token.Position;

public class InterpreterVisitorV2Test {

  @Test
  public void testReadInputNumberDouble() {
    String input = "42.0";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    String code = "readInput();";
    Interpreter interpreter = new Interpreter("1.1");
    VariablesRepository repository = interpreter.executeProgram(code);

    assertEquals(
        42.0,
        repository
            .getVariable(new Identifier("readInput", new Position(0, 0), new Position(0, 0)))
            .value());
  }

  @Test
  public void testReadInputNumberInt() {
    String input = "42";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    String code = "readInput();";
    Interpreter interpreter = new Interpreter("1.1");
    VariablesRepository repository = interpreter.executeProgram(code);

    assertEquals(
        42,
        repository
            .getVariable(new Identifier("readInput", new Position(0, 0), new Position(0, 0)))
            .value());
  }

  @Test
  public void testReadInputString() {
    String input = "Hello, world!";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    String code = "readInput();";
    Interpreter interpreter = new Interpreter("1.1");
    VariablesRepository repository = interpreter.executeProgram(code);

    assertEquals(
        "Hello, world!",
        repository
            .getVariable(new Identifier("readInput", new Position(0, 0), new Position(0, 0)))
            .value());
  }

  @Test
  public void testReadInputBoolean() {
    String input = "true";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    String code = "readInput();";
    Interpreter interpreter = new Interpreter("1.1");
    VariablesRepository repository = interpreter.executeProgram(code);

    assertEquals(
        true,
        repository
            .getVariable(new Identifier("readInput", new Position(0, 0), new Position(0, 0)))
            .value());
  }

  @Test
  public void testReadEnv() {
    String code = "readEnv(\"GRAVITY\");";
    Interpreter interpreter = new Interpreter("1.1");
    VariablesRepository repository = interpreter.executeProgram(code);

    String expectedValue = System.getenv("GRAVITY");
    assertEquals(
        expectedValue,
        repository
            .getVariable(new Identifier("readEnv", new Position(0, 0), new Position(0, 0)))
            .value());
  }

  @Test
  public void testReadEnvNonExistent() {
    String code = "readEnv(\"NON_EXISTENT_ENV_VAR\");";
    Interpreter interpreter = new Interpreter("1.1");
    assertThrows(IllegalArgumentException.class, () -> interpreter.executeProgram(code));
  }

  @Test
  public void testReadInputEmpty() {
    String input = "\n\nhola";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    String code = "readInput();";
    Interpreter interpreter = new Interpreter("1.1");
    VariablesRepository repository = interpreter.executeProgram(code);

    assertEquals(
        "hola",
        repository
            .getVariable(new Identifier("readInput", new Position(0, 0), new Position(0, 0)))
            .value());
  }
}