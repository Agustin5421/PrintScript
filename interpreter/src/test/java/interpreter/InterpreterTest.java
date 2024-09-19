package interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import output.OutputListString;
import output.OutputMock;
import output.OutputString;
import parsers.Parser;

public class InterpreterTest {
  private Parser getParser(String code) {
    Lexer lexer = LexerFactory.getLexer("1.0");
    try {
      lexer = Objects.requireNonNull(lexer).setInput(new ByteArrayInputStream(code.getBytes()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Parser parser = ParserFactory.getParser("1.0");
    return parser.setLexer(lexer);
  }

  @Test
  public void testExecuteProgram() {
    String code = "let x: string = \"this is a string\";";
    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.0", new OutputMock());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();
    assertEquals(
        "this is a string", repository.getNewVariable(new VariableIdentifier("x")).value());
  }

  @Test
  public void testExecuteProgramWithNumber() {
    String code = "let x: number = 42;";
    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.0", new OutputMock());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();
    assertEquals(42, repository.getNewVariable(new VariableIdentifier("x")).value());
  }

  @Test
  public void testExecuteProgramWithMultipleStatements() {
    String code = "let x: string = \"this is a string\"; let y: number = 42;";
    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.0", new OutputMock());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();
    assertEquals(
        "this is a string", repository.getNewVariable(new VariableIdentifier("x")).value());
    assertEquals(42, repository.getNewVariable(new VariableIdentifier("y")).value());
  }

  @Test
  public void testExecuteProgramWithMultipleStatementsAndVariableUpdate() {
    String code = "let x: string = \"this is a string\"; let x: number = 42;";
    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.0", new OutputMock());

    assertThrows(
        Exception.class,
        () -> {
          while (parser.hasNext()) {
            interpreter.interpretNext(parser.next());
          }
        });
  }

  @Test
  public void testExecuteEmptyProgram() {
    String code = "";

    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.0", new OutputMock());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();
    assertEquals(0, repository.getNewVariables().size());
  }

  @Test
  public void testPrinting() {
    String code = "let x: string = \"this is a string\"; println(x);";
    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.0", new OutputMock());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();
    assertEquals(
        "this is a string", repository.getNewVariable(new VariableIdentifier("x")).value());
  }

  @Test
  public void testBinaryExpressionPrinting() {
    String code = "let x: number = 42.5; let y: number = x + 42.5; println(y); println(x);";
    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.0", new OutputMock());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();
    assertEquals(42.5, repository.getNewVariable(new VariableIdentifier("x")).value());

    //  no esta sumando bien, dice q x es 0
    assertEquals(85.0, repository.getNewVariable(new VariableIdentifier("y")).value());
  }

  @Test
  public void testBinaryExpressionPrintForReal() {
    String code = "let x: number = 42.5; println(x + 42.5);";
    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.0", new OutputString());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    OutputString output = (OutputString) engine.getOutputResult();
    assertEquals("85.0", output.getResult());
  }

  @Test
  public void testBinaryStringSumPrint() {
    String code = "println(\"Hello \" + \"World\");";
    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.0", new OutputString());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    OutputString output = (OutputString) engine.getOutputResult();
    assertEquals("Hello World", output.getResult());
  }

  @Test
  public void testBinaryExpressionStringAndNumberPrint() {
    String code = "let x: number = 42.5; println(\"a\" + x);";
    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.0", new OutputString());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    OutputString output = (OutputString) engine.getOutputResult();
    assertEquals("a42.5", output.getResult());
  }

  @Test
  public void testStringsIllegalOperation() {
    String code = "let x: string = \"a\"; println(\"b\" - x);";
    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.0", new OutputString());

    assertThrows(
        Exception.class,
        () -> {
          while (parser.hasNext()) {
            interpreter.interpretNext(parser.next());
          }
        });
  }

  @Test
  public void testNoValueDeclaration() {
    String code = "let x: string;";
    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.0", new OutputMock());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();
    VariableIdentifier varId = new VariableIdentifier("x");
    assertNull(
        repository.getNewVariable(varId),
        "Variable x should not be initialized and should be null by default.");
  }

  @Test
  public void testNoAdditionalQuotes() {
    String code = "let x: string = \"hello\";";
    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.0", new OutputMock());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();
    assertEquals("hello", repository.getNewVariable(new VariableIdentifier("x")).value());
  }

  @Test
  public void testPrintsLog() {
    String code = """
            println("Hello");
            println("World");
            """;
    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.0", new OutputListString());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    OutputListString output = (OutputListString) engine.getOutputResult();
    assertEquals("Hello", output.getSavedResults().get(0));
    assertEquals("World", output.getSavedResults().get(1));
  }
}
