package interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import factory.LexerFactory;
import factory.ParserFactory;
import interpreter.engine.InterpreterEngine;
import interpreter.engine.repository.VariableIdentifier;
import interpreter.engine.repository.VariablesRepository;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;
import lexer.Lexer;
import org.junit.jupiter.api.Test;
import output.OutputMock;
import parsers.Parser;

public class InterpreterIfStatementTest {
  private Parser getParser(String code) {
    Lexer lexer = LexerFactory.getLexer("1.1");
    try {
      lexer = Objects.requireNonNull(lexer).setInput(new ByteArrayInputStream(code.getBytes()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Parser parser = ParserFactory.getParser("1.1");
    return parser.setLexer(lexer);
  }

  @Test
  public void testExecuteProgram() {
    String code =
        "let hola: number = 1; if (true) { if(true){hola=2;} let name: string = \"Oliver\";} else {hola=3; hola=5; hola=6;}";

    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.1", new OutputMock());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();
    assertEquals(2, repository.getNewVariable(new VariableIdentifier("hola")).value());
  }

  @Test
  public void testExecuteProgram2() {
    String code =
        "let hola: number = 1; if (false) { let name: string = \"Oliver\";} else {hola=3; hola=5; hola=6;}";

    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.1", new OutputMock());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();
    assertEquals(6, repository.getNewVariable(new VariableIdentifier("hola")).value());
  }

  @Test
  public void testExecuteProgram5() {
    String code =
        "let hola: number = 1; if (true) { let name: string = \"Oliver\";} else {hola=3; hola=5; hola=6;}";

    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.1", new OutputMock());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();
    assertEquals(1, repository.getNewVariable(new VariableIdentifier("hola")).value());
  }

  @Test
  public void testExecuteProgram6() {
    String code =
        "let hola: number = 1; if (false) { if(true){hola=2;} let name: string = \"Oliver\";} else {hola=3; hola=5; hola=6;}";

    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.1", new OutputMock());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();

    assertEquals(6, repository.getNewVariable(new VariableIdentifier("hola")).value());
  }

  @Test
  public void testExecuteProgram7() {
    String code = "let x: string = \"this is a string\"; if (true) {x = \"omg it worked\";}";

    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.1", new OutputMock());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();

    assertEquals("omg it worked", repository.getNewVariable(new VariableIdentifier("x")).value());
  }

  @Test
  public void testExecuteProgram8() {
    String code = "let x: string; if (true) {x = \"omg it worked\";}";

    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.1", new OutputMock());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();
    assertEquals("omg it worked", repository.getNewVariable(new VariableIdentifier("x")).value());
  }
}
