package interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ast.identifier.Identifier;
import ast.literal.StringLiteral;
import ast.root.AstNode;
import ast.root.Program;
import ast.statements.CallExpression;
import interpreter.visitor.InterpreterVisitor;
import interpreter.visitor.InterpreterVisitorV1;
import interpreter.visitor.InterpreterVisitorV2;
import java.io.ByteArrayInputStream;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import token.Position;

public class InterpreterVisitorV2Test {

  @Test
  public void testReadInputNumberDouble() {
    String input = "42.0";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    Position defaultPosition = new Position(0, 0);
    Identifier methodIdentifier = new Identifier("readInput", defaultPosition, defaultPosition);
    CallExpression callExpression =
        new CallExpression(methodIdentifier, List.of(), false, defaultPosition, defaultPosition);

    List<AstNode> statements = List.of(callExpression);
    Program program = new Program(statements);

    InterpreterVisitor visitor =
        new InterpreterVisitorV2(
            new InterpreterVisitorV1(new VariablesRepository()), new VariablesRepository());
    Interpreter interpreter = new Interpreter(visitor);
    VariablesRepository repository = interpreter.executeProgram(program);

    assertEquals(42.0, repository.getVariable(methodIdentifier).value());
  }

  @Test
  public void testReadInputNumberInt() {
    String input = "42";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    Position defaultPosition = new Position(0, 0);
    Identifier methodIdentifier = new Identifier("readInput", defaultPosition, defaultPosition);
    CallExpression callExpression =
        new CallExpression(methodIdentifier, List.of(), false, defaultPosition, defaultPosition);

    List<AstNode> statements = List.of(callExpression);
    Program program = new Program(statements);

    InterpreterVisitor visitor =
        new InterpreterVisitorV2(
            new InterpreterVisitorV1(new VariablesRepository()), new VariablesRepository());
    Interpreter interpreter = new Interpreter(visitor);
    VariablesRepository repository = interpreter.executeProgram(program);

    assertEquals(42, repository.getVariable(methodIdentifier).value());
  }

  @ParameterizedTest
  @ValueSource(strings = {"true", "TRUE", "t", "T", "false", "FALSE", "f", "F"})
  public void testReadInputBoolean(String input) {
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    Position defaultPosition = new Position(0, 0);
    Identifier methodIdentifier = new Identifier("readInput", defaultPosition, defaultPosition);
    CallExpression callExpression =
        new CallExpression(methodIdentifier, List.of(), false, defaultPosition, defaultPosition);

    List<AstNode> statements = List.of(callExpression);
    Program program = new Program(statements);

    InterpreterVisitor visitor =
        new InterpreterVisitorV2(
            new InterpreterVisitorV1(new VariablesRepository()), new VariablesRepository());
    Interpreter interpreter = new Interpreter(visitor);
    VariablesRepository repository = interpreter.executeProgram(program);

    boolean expectedValue = input.equalsIgnoreCase("true") || input.equalsIgnoreCase("t");
    assertEquals(expectedValue, repository.getVariable(methodIdentifier).value());
  }

  @Test
  public void testReadInputString() {
    String input = "hello";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    Position defaultPosition = new Position(0, 0);
    Identifier methodIdentifier = new Identifier("readInput", defaultPosition, defaultPosition);
    CallExpression callExpression =
        new CallExpression(methodIdentifier, List.of(), false, defaultPosition, defaultPosition);

    List<AstNode> statements = List.of(callExpression);
    Program program = new Program(statements);

    InterpreterVisitor visitor =
        new InterpreterVisitorV2(
            new InterpreterVisitorV1(new VariablesRepository()), new VariablesRepository());
    Interpreter interpreter = new Interpreter(visitor);
    VariablesRepository repository = interpreter.executeProgram(program);

    assertEquals("hello", repository.getVariable(methodIdentifier).value());
  }

  @Test
  public void testReadInputStringSpace() {
    String input = " ";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    Position defaultPosition = new Position(0, 0);
    Identifier methodIdentifier = new Identifier("readInput", defaultPosition, defaultPosition);
    CallExpression callExpression =
        new CallExpression(methodIdentifier, List.of(), false, defaultPosition, defaultPosition);

    List<AstNode> statements = List.of(callExpression);
    Program program = new Program(statements);

    InterpreterVisitor visitor =
        new InterpreterVisitorV2(
            new InterpreterVisitorV1(new VariablesRepository()), new VariablesRepository());
    Interpreter interpreter = new Interpreter(visitor);
    VariablesRepository repository = interpreter.executeProgram(program);

    assertEquals(" ", repository.getVariable(methodIdentifier).value());
  }

  @Test
  public void testReadInputStringWithNothing() {
    String input = "\nvalidInput";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    Position defaultPosition = new Position(0, 0);
    Identifier methodIdentifier = new Identifier("readInput", defaultPosition, defaultPosition);
    CallExpression callExpression =
        new CallExpression(methodIdentifier, List.of(), false, defaultPosition, defaultPosition);

    List<AstNode> statements = List.of(callExpression);
    Program program = new Program(statements);

    InterpreterVisitor visitor =
        new InterpreterVisitorV2(
            new InterpreterVisitorV1(new VariablesRepository()), new VariablesRepository());
    Interpreter interpreter = new Interpreter(visitor);
    VariablesRepository repository = interpreter.executeProgram(program);

    assertEquals("validInput", repository.getVariable(methodIdentifier).value());
  }

  @Test
  public void testReadEnvNumber() {
    Position defaultPosition = new Position(0, 0);
    Identifier methodIdentifier = new Identifier("readEnv", defaultPosition, defaultPosition);
    StringLiteral argument = new StringLiteral("GRAVITY", defaultPosition, defaultPosition);
    CallExpression callExpression =
        new CallExpression(
            methodIdentifier, List.of(argument), false, defaultPosition, defaultPosition);

    List<AstNode> statements = List.of(callExpression);
    Program program = new Program(statements);

    InterpreterVisitor visitor =
        new InterpreterVisitorV2(
            new InterpreterVisitorV1(new VariablesRepository()), new VariablesRepository());
    Interpreter interpreter = new Interpreter(visitor);
    VariablesRepository repository = interpreter.executeProgram(program);

    assertEquals(9.81, repository.getVariable(methodIdentifier).value());
  }

  @Test
  public void testReadEnvBoolean() {
    Position defaultPosition = new Position(0, 0);
    Identifier methodIdentifier = new Identifier("readEnv", defaultPosition, defaultPosition);
    StringLiteral argument = new StringLiteral("IS_CONSTANT", defaultPosition, defaultPosition);
    CallExpression callExpression =
        new CallExpression(
            methodIdentifier, List.of(argument), false, defaultPosition, defaultPosition);

    List<AstNode> statements = List.of(callExpression);
    Program program = new Program(statements);

    InterpreterVisitor visitor =
        new InterpreterVisitorV2(
            new InterpreterVisitorV1(new VariablesRepository()), new VariablesRepository());
    Interpreter interpreter = new Interpreter(visitor);
    VariablesRepository repository = interpreter.executeProgram(program);

    assertEquals(true, repository.getVariable(methodIdentifier).value());
  }

  @Test
  public void testReadEnvString() {
    Position defaultPosition = new Position(0, 0);
    Identifier methodIdentifier = new Identifier("readEnv", defaultPosition, defaultPosition);
    StringLiteral argument =
        new StringLiteral("UNIVERSAL_CONSTANT", defaultPosition, defaultPosition);
    CallExpression callExpression =
        new CallExpression(
            methodIdentifier, List.of(argument), false, defaultPosition, defaultPosition);

    List<AstNode> statements = List.of(callExpression);
    Program program = new Program(statements);

    InterpreterVisitor visitor =
        new InterpreterVisitorV2(
            new InterpreterVisitorV1(new VariablesRepository()), new VariablesRepository());
    Interpreter interpreter = new Interpreter(visitor);
    VariablesRepository repository = interpreter.executeProgram(program);

    assertEquals("constant", repository.getVariable(methodIdentifier).value());
  }

  @Test
  public void testReadEnvVariableNotFound() {
    Position defaultPosition = new Position(0, 0);
    Identifier methodIdentifier = new Identifier("readEnv", defaultPosition, defaultPosition);
    StringLiteral argument =
        new StringLiteral("NON_EXISTENT_VAR", defaultPosition, defaultPosition);
    CallExpression callExpression =
        new CallExpression(
            methodIdentifier, List.of(argument), false, defaultPosition, defaultPosition);

    List<AstNode> statements = List.of(callExpression);
    Program program = new Program(statements);

    InterpreterVisitor visitor =
        new InterpreterVisitorV2(
            new InterpreterVisitorV1(new VariablesRepository()), new VariablesRepository());
    Interpreter interpreter = new Interpreter(visitor);

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          interpreter.executeProgram(program);
        });
  }
}
