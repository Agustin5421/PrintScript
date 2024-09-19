package interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import exceptions.MismatchTypeException;
import factory.LexerFactory;
import factory.ParserFactory;
import interpreter.engine.InterpreterEngine;
import interpreter.engine.repository.VariableIdentifier;
import interpreter.engine.repository.VariablesRepository;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;
import lexer.Lexer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import output.OutputMock;
import parsers.Parser;

public class InterpreterEngineV1Test {
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
  public void testAssignmentExpression() {
    String code = "let name: string = \"value\"; name = \"otherValue\";";
    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.0", new OutputMock());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();
    assertEquals("otherValue", repository.getNewVariable(new VariableIdentifier("name")).value());
  }

  @Test
  public void testSimpleNodes() {
    String code = "let name: string = \"value\"; let number: number = 42;";
    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.0", new OutputMock());

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
  public void testInitializedVar() {
    String code =
        """
            let pi: number;
            pi = 3.14;
            println(pi / 2);
            """;
    Parser parser = getParser(code);
    Interpreter interpreter = InterpreterFactory.buildInterpreter("1.0", new OutputMock());

    while (parser.hasNext()) {
      interpreter = interpreter.interpretNext(parser.next());
    }

    InterpreterEngine engine = interpreter.interpreterEngine();
    VariablesRepository repository = engine.getVariablesRepository();
    assertEquals(3.14, repository.getNewVariable(new VariableIdentifier("pi")).value());
  }

  @Test
  public void constVarTest() {
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
}
