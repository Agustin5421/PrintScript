package interpreter;

import static org.junit.jupiter.api.Assertions.*;

import exceptions.MismatchTypeException;
import interpreter.engine.InterpreterEngine;
import interpreter.engine.repository.VariableIdentifier;
import interpreter.engine.repository.VariablesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import output.OutputListString;
import output.OutputMock;
import output.OutputResult;
import output.OutputString;
import parsers.Parser;

public abstract class CommonInterpreterTest {
  private final OutputMock output = new OutputMock();

  protected abstract Parser getParser(String code);

  protected abstract Interpreter getInterpreter(OutputResult<String> output);

  @Test
  public void testExecuteEmptyProgram() {
    String code = "";

    Parser parser = getParser(code);
    Interpreter interpreter = getInterpreter(output);

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();
    assertEquals(0, repository.getNewVariables().size());
  }

  @Test
  public void testVariableDeclarationWithString() {
    String code = "let x: string = \"this is a string\";";
    Parser parser = getParser(code);
    Interpreter interpreter = getInterpreter(output);

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();
    assertEquals(
        "this is a string", repository.getNewVariable(new VariableIdentifier("x")).value());
  }

  @Test
  public void testVariableDeclarationWithNumber() {
    String code = "let x: number = 42;";
    Parser parser = getParser(code);
    Interpreter interpreter = getInterpreter(output);

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();
    assertEquals(42, repository.getNewVariable(new VariableIdentifier("x")).value());
  }

  @Test
  public void testSeveralVariableDeclaration() {
    String code = "let name: string = \"value\"; let number: number = 42;";
    Parser parser = getParser(code);
    Interpreter interpreter = getInterpreter(output);

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();
    assertEquals("value", repository.getNewVariable(new VariableIdentifier("name")).value());
    Assertions.assertEquals(
        42, repository.getNewVariable(new VariableIdentifier("number")).value());
  }

  @Test
  public void failSameVariableDeclaration() {
    String code = "let x: string = \"this is a string\"; let x: number = 42;";
    Parser parser = getParser(code);
    Interpreter interpreter = getInterpreter(output);

    assertThrows(
        Exception.class,
        () -> {
          while (parser.hasNext()) {
            interpreter.interpretNext(parser.next());
          }
        });
  }

  @Test
  public void failTypeMismatchDeclaration() {
    String code = "let pi: number = \"hola\";";
    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.0", new OutputMock());

    assertThrows(
        MismatchTypeException.class,
        () -> {
          while (parser.hasNext()) {
            interpreter.interpretNext(parser.next());
          }
        });
  }

  @Test
  public void testVariableNullInitialization() {
    String code = "let x: string;";
    Parser parser = getParser(code);
    Interpreter interpreter = getInterpreter(output);

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();
    VariableIdentifier varId = new VariableIdentifier("x");
    assertNull(repository.getNewVariable(varId));
  }

  @Test
  public void testAssignmentExpression() {
    String code = "let name: string = \"value\"; name = \"otherValue\";";
    Parser parser = getParser(code);
    Interpreter interpreter = getInterpreter(output);

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();
    assertEquals("otherValue", repository.getNewVariable(new VariableIdentifier("name")).value());
  }

  @Test
  public void testAssignmentExpressionWithNullInitializedVariable() {
    String code =
        """
            let pi: number;
            pi = 3.14;
            println(pi / 2);
            """;
    Parser parser = getParser(code);
    Interpreter interpreter = getInterpreter(new OutputString());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();
    assertEquals(3.14, repository.getNewVariable(new VariableIdentifier("pi")).value());

    OutputString output = (OutputString) engine.getOutputResult();
    assertEquals("1.57", output.getResult());
  }

  @Test
  public void testPrinting() {
    String code = "let x: string = \"this is a string\"; println(x);";
    Parser parser = getParser(code);
    Interpreter interpreter = getInterpreter(new OutputString());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    OutputString output = (OutputString) engine.getOutputResult();
    assertEquals("this is a string", output.getResult());
  }

  @Test
  public void testSeveralPrints() {
    String code = """
            println("Hello");
            println("World");
            """;
    Parser parser = getParser(code);
    Interpreter interpreter = getInterpreter(new OutputListString());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    OutputListString output = (OutputListString) engine.getOutputResult();
    assertEquals("Hello", output.getSavedResults().get(0));
    assertEquals("World", output.getSavedResults().get(1));
  }

  @Test
  public void testBinaryExpressionPrinting() {
    String code = "let x: number = 42.5; println(x + 42.5);";
    Parser parser = getParser(code);
    Interpreter interpreter = getInterpreter(new OutputString());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    OutputString output = (OutputString) engine.getOutputResult();
    assertEquals("85.0", output.getResult());
  }

  @Test
  public void testBinaryExpressionPrintingStringAndNumber() {
    String code = "let x: number = 42.5; println(\"a\" + x);";
    Parser parser = getParser(code);
    Interpreter interpreter = getInterpreter(new OutputString());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    OutputString output = (OutputString) engine.getOutputResult();
    assertEquals("a42.5", output.getResult());
  }

  @Test
  public void failStringsIllegalOperation() {
    String code = "let x: string = \"a\"; println(\"b\" - x);";
    Parser parser = getParser(code);
    Interpreter interpreter = getInterpreter(output);

    assertThrows(
        Exception.class,
        () -> {
          while (parser.hasNext()) {
            interpreter.interpretNext(parser.next());
          }
        });
  }
}
