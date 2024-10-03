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
import output.OutputResult;
import output.OutputString;
import parsers.Parser;

public class InterpreterV2Test extends CommonInterpreterTest {
  private final OutputMock output = new OutputMock();

  @Override
  protected Parser getParser(String code) {
    Lexer lexer = LexerFactory.getLexer("1.1");
    try {
      lexer = Objects.requireNonNull(lexer).setInput(new ByteArrayInputStream(code.getBytes()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Parser parser = ParserFactory.getParser("1.1");
    return parser.setLexer(lexer);
  }

  @Override
  protected Interpreter getInterpreter(OutputResult<String> output) {
    return InterpreterFactory.buildInterpreter("1.1", output);
  }

  @Test
  public void testConstantDeclaration() {
    String code = "const x: number = 42;";
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
  public void failBinaryExpressionWithBooleans() {
    String code = "let x: boolean = true + false;";
    Parser parser = getParser(code);
    Interpreter interpreter = getInterpreter(output);

    assertThrows(
        UnsupportedOperationException.class,
        () -> {
          while (parser.hasNext()) {
            interpreter.interpretNext(parser.next());
          }
        });
  }

  @Test
  public void testConditionalPrints() {
    String code =
        """
                    const booleanValue: boolean = true;
                    if(booleanValue) {
                        println("if statement working correctly");
                    }
                    println("outside of conditional");
                    """;
    Parser parser = getParser(code);
    Interpreter interpreter = getInterpreter(new OutputListString());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    OutputListString output = (OutputListString) engine.getOutputResult();

    assertEquals("if statement working correctly", output.getSavedResults().get(0));
    assertEquals("outside of conditional", output.getSavedResults().get(1));
  }

  @Test
  public void testReadInputNumberDouble() {
    String code = "let x: number = readInput(\"Name:\");";

    String input = "42.0";
    Inputs.setInputs(new InputQueue(new ArrayDeque<>(List.of(input))));

    Parser parser = getParser(code);
    Interpreter interpreter = getInterpreter(output);

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();

    assertEquals(42.0, repository.getNewVariable(new VariableIdentifier("x")).value());
  }

  @Test
  public void testReadInputNumberInt() {
    String code = "let x: number = readInput(\"Name:\");";

    String input = "42";
    Inputs.setInputs(new InputQueue(new ArrayDeque<>(List.of(input))));

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
  public void testReadInputString() {
    String code = "let x: string = readInput(\"Name:\");";

    String input = "Hello, World!";
    Inputs.setInputs(new InputQueue(new ArrayDeque<>(List.of(input))));

    Parser parser = getParser(code);
    Interpreter interpreter = getInterpreter(output);

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();

    assertEquals("Hello, World!", repository.getNewVariable(new VariableIdentifier("x")).value());
  }

  @Test
  public void testReadInputBoolean() {
    String code = "let x: boolean = readInput(\"Name:\");";

    String input = "true";
    Inputs.setInputs(new InputQueue(new ArrayDeque<>(List.of(input))));

    Parser parser = getParser(code);
    Interpreter interpreter = getInterpreter(output);

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();

    assertEquals(true, repository.getNewVariable(new VariableIdentifier("x")).value());
  }

  @Test
  public void failReadInputEmpty() {
    String code = "let x: number = readInput(\"Name:\");";
    Inputs.setInputs(new InputQueue(new ArrayDeque<>()));

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
  public void testReadEnvExistingVariables() {
    String code =
        """
        let w: string = readEnv("UNIVERSAL_CONSTANT");
        let x: boolean = readEnv("IS_CONSTANT");
        let y: number = readEnv("GRAVITY");
        let z: number = readEnv("PLANCK_CONSTANT");""";

    Parser parser = getParser(code);
    Interpreter interpreter = getInterpreter(new OutputString());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();

    assertEquals("constant", repository.getNewVariable(new VariableIdentifier("w")).value());
    assertEquals(true, repository.getNewVariable(new VariableIdentifier("x")).value());
    assertEquals(9.81, repository.getNewVariable(new VariableIdentifier("y")).value());
    assertEquals(6.62607015e-34, repository.getNewVariable(new VariableIdentifier("z")).value());

    OutputString output = (OutputString) engine.getOutputResult();
    assertEquals("", output.getResult());
  }

  @Test
  public void failReadEnvNonExistent() {
    String code = "readEnv(\"NON_EXISTENT_ENV_VAR\");";
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
