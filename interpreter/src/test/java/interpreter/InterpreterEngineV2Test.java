package interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import factory.LexerFactory;
import factory.ParserFactory;
import input.InputQueue;
import interpreter.engine.InterpreterEngine;
import interpreter.engine.repository.VariableIdentifier;
import interpreter.engine.repository.VariablesRepository;
import interpreter.engine.staticprovider.Inputs;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Objects;
import lexer.Lexer;
import org.junit.jupiter.api.Test;
import output.OutputListString;
import output.OutputMock;
import parsers.Parser;

public class InterpreterEngineV2Test {
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
  public void testReadInputNumberDouble() {
    String code = "let x: number = readInput(\"Name:\");";

    String input = "42.0";
    Inputs.setInputs(null);
    Inputs.setInputs(new InputQueue(new ArrayDeque<>(List.of(input))));

    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.1", new OutputMock());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();

    assertEquals(42.0, repository.getNewVariable(new VariableIdentifier("x")).value());
    Inputs.setInputs(null);
  }

  @Test
  public void testReadInputNumberInt() {
    String code = "let x: number = readInput(\"Name:\");";

    String input = "42";
    Inputs.setInputs(null);
    Inputs.setInputs(new InputQueue(new ArrayDeque<>(List.of(input))));

    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.1", new OutputMock());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();

    assertEquals(42, repository.getNewVariable(new VariableIdentifier("x")).value());

    Inputs.setInputs(null);
  }

  @Test
  public void testReadInputString() {
    String code = "let x: string = readInput(\"Name:\");";
    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.1", new OutputMock());

    String input = "Hello, world!";
    Inputs.setInputs(null);
    Inputs.setInputs(new InputQueue(new ArrayDeque<>(List.of(input))));

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();

    assertEquals("Hello, world!", repository.getNewVariable(new VariableIdentifier("x")).value());

    Inputs.setInputs(null);
  }

  @Test
  public void testReadEnvNonExistent() {
    String code = "readEnv(\"NON_EXISTENT_ENV_VAR\");";
    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.1", new OutputMock());

    assertThrows(
        RuntimeException.class,
        () -> {
          while (parser.hasNext()) {
            interpreter.interpretNext(parser.next());
          }
        });
  }

  @Test
  public void testReadInputEmpty() {
    String code = "let x:string =readInput(\"Name:\");";

    String input = "hola";
    Inputs.setInputs(null);
    Inputs.setInputs(new InputQueue(new ArrayDeque<>(List.of(input))));

    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.1", new OutputMock());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();

    assertEquals("hola", repository.getNewVariable(new VariableIdentifier("x")).value());
    Inputs.setInputs(null);
  }

  @Test
  public void testReadInputBooleanTrue() {
    String code = "let x:boolean = readInput(\"Name:\");";

    String input = "true";
    Inputs.setInputs(null);
    Inputs.setInputs(new InputQueue(new ArrayDeque<>(List.of(input))));

    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.1", new OutputMock());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();

    assertEquals(true, repository.getNewVariable(new VariableIdentifier("x")).value());
    Inputs.setInputs(null);
  }

  @Test
  public void testPrintList() {
    String code =
        """
                    const booleanValue: boolean = true;
                    if(booleanValue) {
                        println("if statement working correctly");
                    }
                    println("outside of conditional");
                    """;
    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.1", new OutputListString());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    OutputListString output = (OutputListString) engine.getOutputResult();

    assertEquals("if statement working correctly", output.getSavedResults().get(0));
    assertEquals("outside of conditional", output.getSavedResults().get(1));
  }

  @Test
  public void testPrintsLog() {
    String code = """
            println("Hello");
            println("World");
            """;

    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.1", new OutputListString());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    OutputListString output = (OutputListString) engine.getOutputResult();

    assertEquals("Hello", output.getSavedResults().get(0));
    assertEquals("World", output.getSavedResults().get(1));
  }

  @Test
  public void testReadEnvExistingVariable() {
    String code = "let x: string = readEnv(\"UNIVERSAL_CONSTANT\");";

    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.1", new OutputListString());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();

    assertEquals("constant", repository.getNewVariable(new VariableIdentifier("x")).value());
  }

  @Test
  public void testReadEnvExistingBooleanVariable() {
    String code = "let x: boolean = readEnv(\"IS_CONSTANT\");";

    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.1", new OutputListString());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();

    assertEquals(true, repository.getNewVariable(new VariableIdentifier("x")).value());
  }

  @Test
  public void testReadEnvExistingIntegerVariable() {
    String code = "let x: number = readEnv(\"GRAVITY\");";

    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.1", new OutputListString());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();

    assertEquals(9.81, repository.getNewVariable(new VariableIdentifier("x")).value());
  }

  @Test
  public void testReadEnvExistingDoubleVariable() {
    String code = "let x: number = readEnv(\"PLANCK_CONSTANT\");";

    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.1", new OutputListString());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();

    assertEquals(6.62607015e-34, repository.getNewVariable(new VariableIdentifier("x")).value());
  }

  @Test
  public void testArithOp2() {
    String code = "let numberResult: number = 5 * 5 - 8; println(numberResult);";

    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.1", new OutputListString());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();

    assertEquals(17, repository.getNewVariable(new VariableIdentifier("numberResult")).value());
  }
}
