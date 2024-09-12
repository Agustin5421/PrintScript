package interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import interpreter.visitor.repository.VariableIdentifier;
import interpreter.visitor.repository.VariablesRepository;
import org.junit.jupiter.api.Test;

public class InterpreterIfStatementTest {
  @Test
  public void testExecuteProgram() {
    String code =
        "let hola: number = 1; if (true) { if(true){hola=2;} let name: string = \"Oliver\";} else {hola=3; hola=5; hola=6;}";

    String code2 = "let x: string = \"this is a string\"; if (true) {x = \"omg it worked\";}";
    Interpreter interpreter = new Interpreter("1.1");
    VariablesRepository repository = interpreter.executeProgram(code);
    assertEquals(2, repository.getNewVariable(new VariableIdentifier("hola")).value());
  }

  @Test
  public void testExecuteProgram2() {
    String code =
        "let hola: number = 1; if (false) { let name: string = \"Oliver\";} else {hola=3; hola=5; hola=6;}";

    String code2 = "let x: string = \"this is a string\"; if (true) {x = \"omg it worked\";}";
    Interpreter interpreter = new Interpreter("1.1");
    VariablesRepository repository = interpreter.executeProgram(code);
    assertEquals(6, repository.getNewVariable(new VariableIdentifier("hola")).value());
  }

  @Test
  public void testExecuteProgram5() {
    String code =
        "let hola: number = 1; if (true) { let name: string = \"Oliver\";} else {hola=3; hola=5; hola=6;}";

    String code2 = "let x: string = \"this is a string\"; if (true) {x = \"omg it worked\";}";
    Interpreter interpreter = new Interpreter("1.1");
    VariablesRepository repository = interpreter.executeProgram(code);
    assertEquals(1, repository.getNewVariable(new VariableIdentifier("hola")).value());
  }

  @Test
  public void testExecuteProgram6() {
    String code =
        "let hola: number = 1; if (false) { if(true){hola=2;} let name: string = \"Oliver\";} else {hola=3; hola=5; hola=6;}";

    String code2 = "let x: string = \"this is a string\"; if (true) {x = \"omg it worked\";}";
    Interpreter interpreter = new Interpreter("1.1");
    VariablesRepository repository = interpreter.executeProgram(code);
    assertEquals(6, repository.getNewVariable(new VariableIdentifier("hola")).value());
  }
}
