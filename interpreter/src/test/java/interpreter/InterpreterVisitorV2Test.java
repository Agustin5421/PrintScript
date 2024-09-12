package interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ast.identifier.Identifier;
import interpreter.factory.InterpreterFactory;
import interpreter.visitor.repository.VariablesRepository;
import java.io.ByteArrayInputStream;
import java.util.List;
import org.junit.jupiter.api.Test;
import token.Position;

public class InterpreterVisitorV2Test {

  @Test
  public void testReadInputNumberDouble() {
    String input = "42.0";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    String code = "readInput();";
    Interpreter interpreter = InterpreterFactory.getInterpreter("1.1");
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

  @Test
  public void testReadInputBooleanTrue() {
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
  public void testReadInputBooleanFalse() {
    String input = "false";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    String code = "readInput();";
    Interpreter interpreter = new Interpreter("1.1");
    VariablesRepository repository = interpreter.executeProgram(code);

    assertEquals(
        false,
        repository
            .getVariable(new Identifier("readInput", new Position(0, 0), new Position(0, 0)))
            .value());
  }

  @Test
  public void testReadInputBooleanT() {
    String input = "t";
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
  public void testReadInputBooleanF() {
    String input = "f";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    String code = "readInput();";
    Interpreter interpreter = new Interpreter("1.1");
    VariablesRepository repository = interpreter.executeProgram(code);

    assertEquals(
        false,
        repository
            .getVariable(new Identifier("readInput", new Position(0, 0), new Position(0, 0)))
            .value());
  }

  @Test
  public void testReadInputNumberDouble2() {
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
  public void testReadInputNumberInt2() {
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
  public void testReadInputStringWithSpaces() {
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
  public void testReadInputStringWithoutSpaces() {
    String input = "HelloWorld";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    String code = "readInput();";
    Interpreter interpreter = new Interpreter("1.1");
    VariablesRepository repository = interpreter.executeProgram(code);

    assertEquals(
        "HelloWorld",
        repository
            .getVariable(new Identifier("readInput", new Position(0, 0), new Position(0, 0)))
            .value());
  }

  @Test
  public void testReadInputStringWithWords() {
    String input = "Hello world this is a test";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    String code = "readInput();";
    Interpreter interpreter = new Interpreter("1.1");
    VariablesRepository repository = interpreter.executeProgram(code);

    assertEquals(
        "Hello world this is a test",
        repository
            .getVariable(new Identifier("readInput", new Position(0, 0), new Position(0, 0)))
            .value());
  }

  @Test
  public void testPrintlnString() {
    String code = "println(\"Hello, world!\");";
    VariablesRepository variablesRepository = new VariablesRepository();
    Interpreter interpreter = new Interpreter("1.1");
    List<String> printedValues = interpreter.interpret(code);

    assertEquals("Hello, world!", printedValues.get(0));
  }

  @Test
  public void testPrintlnNumber() {
    String code = "println(42);";
    VariablesRepository variablesRepository = new VariablesRepository();
    Interpreter interpreter = new Interpreter("1.1");
    List<String> printedValues = interpreter.interpret(code);

    assertEquals("42", printedValues.get(0));
  }

  @Test
  public void testPrintlnBoolean() {
    String code = "println(\"true\");";
    VariablesRepository variablesRepository = new VariablesRepository();
    Interpreter interpreter = new Interpreter("1.1");
    List<String> printedValues = interpreter.interpret(code);

    assertEquals("true", printedValues.get(0));
  }

  @Test
  public void testPrintlnBinaryExpression() {
    String code = "println(21 + 21);";
    VariablesRepository variablesRepository = new VariablesRepository();
    Interpreter interpreter = new Interpreter("1.1");
    List<String> printedValues = interpreter.interpret(code);

    assertEquals("42", printedValues.get(0));
  }

  @Test
  public void testPrintList() {
    String code =
        "const booleanValue: boolean = true;\n"
            + "if(booleanValue) {\n"
            + "    println(\"if statement working correctly\");\n"
            + "}\n"
            + "println(\"outside of conditional\");\n";
    Interpreter interpreter = new Interpreter("1.1");
    List<String> prints = interpreter.interpret(code);

    assertEquals("if statement working correctly", prints.get(0));
    assertEquals("outside of conditional", prints.get(1));
  }

  //  @Test
  //  public void testReadEnvExistingVariable() {
  //    String code = "let x: string = readEnv(\"UNIVERSAL_CONSTANT\");";
  //    Interpreter interpreter = new Interpreter("1.1");
  //    VariablesRepository newVar = interpreter.executeProgram(code);
  //
  //    assertEquals("constant", newVar.getNewVariable(new VariableIdentifier("x")).value());
  //  }
  //
  //  @Test
  //  public void testReadEnvExistingBooleanVariable() {
  //    String code = "let x: boolean = readEnv(\"IS_CONSTANT\");";
  //    Interpreter interpreter = new Interpreter("1.1");
  //    VariablesRepository newVar = interpreter.executeProgram(code);
  //
  //    assertEquals(true, newVar.getNewVariable(new VariableIdentifier("x")).value());
  //  }
  //
  //  @Test
  //  public void testReadEnvExistingIntegerVariable() {
  //    String code = "let x: number = readEnv(\"GRAVITY\");";
  //    Interpreter interpreter = new Interpreter("1.1");
  //    VariablesRepository newVar = interpreter.executeProgram(code);
  //
  //    assertEquals(9.81, newVar.getNewVariable(new VariableIdentifier("x")).value());
  //  }
  //
  //  @Test
  //  public void testReadEnvExistingDoubleVariable() {
  //    String code = "let x: number = readEnv(\"PLANCK_CONSTANT\");";
  //    Interpreter interpreter = new Interpreter("1.1");
  //    VariablesRepository newVar = interpreter.executeProgram(code);
  //
  //    assertEquals(6.62607015e-34, newVar.getNewVariable(new VariableIdentifier("x")).value());
  //  }
}
