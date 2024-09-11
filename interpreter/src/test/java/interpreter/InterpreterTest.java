package interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import exceptions.UnsupportedExpressionException;
import interpreter.visitor.repository.VariableIdentifier;
import interpreter.visitor.repository.VariablesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import token.Position;

public class InterpreterTest {
  private final Position defaultPosition = new Position(0, 0);

  @Test
  public void testExecuteProgram() {
    String code = "let x: string = \"this is a string\";";
    Interpreter interpreter = new Interpreter("1.0");
    VariablesRepository repository = interpreter.executeProgram(code);
    assertEquals(
        "\"this is a string\"", repository.getNewVariable(new VariableIdentifier("x")).value());
  }

  @Test
  public void testExecuteProgramWithNumber() {
    String code = "let x: number = 42;";
    Interpreter interpreter = new Interpreter("1.0");
    VariablesRepository repository = interpreter.executeProgram(code);
    assertEquals(42, repository.getNewVariable(new VariableIdentifier("x")).value());
  }

  @Test
  public void testExecuteProgramWithMultipleStatements() {
    String code = "let x: string = \"this is a string\"; let y: number = 42;";
    Interpreter interpreter = new Interpreter("1.0");
    VariablesRepository repository = interpreter.executeProgram(code);
    assertEquals(
        "\"this is a string\"", repository.getNewVariable(new VariableIdentifier("x")).value());
    assertEquals(42, repository.getNewVariable(new VariableIdentifier("y")).value());
  }

  @Test
  public void testExecuteProgramWithMultipleStatementsAndVariableUpdate() {
    String code = "let x: string = \"this is a string\"; let x: number = 42;";
    Interpreter interpreter = new Interpreter("1.0");
    assertThrows(IllegalArgumentException.class, () -> interpreter.executeProgram(code));
  }

  @Test
  public void testExecuteEmptyProgram() {
    String code = "";
    Interpreter interpreter = new Interpreter("1.0");
    VariablesRepository repository = interpreter.executeProgram(code);
    assertEquals(0, repository.getNewVariables().size());
  }

  @Test
  public void testPrinting() {
    String code = "let x: string = \"this is a string\"; println(x);";
    Interpreter interpreter = new Interpreter("1.0");
    VariablesRepository repository = interpreter.executeProgram(code);
    assertEquals(
        "\"this is a string\"", repository.getNewVariable(new VariableIdentifier("x")).value());
  }

  @Test
  public void testBinaryExpressionPrinting() {
    String code = "let x: number = 42.5; let y: number = x + 42.5; println(y); println(x);";
    Interpreter interpreter = new Interpreter("1.0");
    VariablesRepository repository = interpreter.executeProgram(code);
    assertEquals(42.5, repository.getNewVariable(new VariableIdentifier("x")).value());

    //  no esta sumando bien, dice q x es 0
    assertEquals(85.0, repository.getNewVariable(new VariableIdentifier("y")).value());
  }

  @Test
  public void testBinaryExpressionPrintForReal() {
    String code = "let x: number = 42.5; println(x + 42.5);";
    Interpreter interpreter = new Interpreter("1.0");
    VariablesRepository repository = interpreter.executeProgram(code);
  }

  @Test
  public void testBinaryStringSumPrint() {
    String code = "println(\"Hello \" + \"World\");";
    Interpreter interpreter = new Interpreter("1.0");
    VariablesRepository repository = interpreter.executeProgram(code);
  }

  @Test
  public void testBinaryExpressionStringAndNumberPrint() {
    String code = "let x: number = 42.5; println(\"a\" + x);";
    Interpreter interpreter = new Interpreter("1.0");
    VariablesRepository repository = interpreter.executeProgram(code);
  }

  @Test
  public void testStringsIllegalOperation() {
    String code = "let x: string = \"a\"; println(\"b\" - x);";
    Interpreter interpreter = new Interpreter("1.0");
    Assertions.assertThrows(
        UnsupportedExpressionException.class, () -> interpreter.executeProgram(code));
  }
}
