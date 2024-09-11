package interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import interpreter.visitor.repository.VariableIdentifier;
import interpreter.visitor.repository.VariablesRepository;
import org.junit.jupiter.api.Test;
import token.Position;

public class InterpreterIfStatementTest {
  private final Position defaultPosition = new Position(0, 0);

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

  //    @Test
  //    public void testExecuteProgram3() {
  //
  //        String code = "let x: string = \"this is a string\"; if (true) {x = \"omg it worked\";}
  // let y:string = \"geda\"";
  //        Interpreter interpreter = new Interpreter("1.1");
  //        VariablesRepository repository = interpreter.executeProgram(code);
  //        assertEquals(
  //                "omg it worked",
  //                repository.getNewVariable(new VariableIdentifier("x")).value());
  //    }
  //
  //    @Test
  //    public void testExecuteProgram4() {
  //        String code = "let x: string = \"this is a string\"; if (false) {x = \"omg it worked\";}
  // let y:string = \"gedi\"";
  //        Interpreter interpreter = new Interpreter("1.1");
  //        VariablesRepository repository = interpreter.executeProgram(code);
  //        assertEquals(
  //                "this is a string",
  //                repository.getNewVariable(new VariableIdentifier("x")).value());
  //    }
}
