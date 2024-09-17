package interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import exceptions.MismatchTypeException;
import factory.LexerFactory;
import factory.ParserFactory;
import interpreter.factory.ReworkedInterpreterFactory;
import interpreter.visitor.InterpreterVisitorV3;
import interpreter.visitor.repository.VariableIdentifier;
import interpreter.visitor.repository.VariablesRepository;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import lexer.Lexer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import output.OutputMock;
import parsers.Parser;

public class InterpreterVisitorTest {
  private Parser getParser(String code) {
    Lexer lexer = LexerFactory.getLexer("1.0");
    try {
      lexer = lexer.setInput(new ByteArrayInputStream(code.getBytes()));
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
    ReworkedInterpreter reworkedInterpreter =
        ReworkedInterpreterFactory.buildInterpreter("1.0", new OutputMock());

    while (parser.hasNext()) {
      reworkedInterpreter = reworkedInterpreter.interpret(parser.next());
    }

    InterpreterVisitorV3 visitor = (InterpreterVisitorV3) reworkedInterpreter.getVisitor();
    VariablesRepository repository = visitor.getVariablesRepository();
    assertEquals("otherValue", repository.getNewVariable(new VariableIdentifier("name")).value());
  }

  @Test
  public void testSimpleNodes() {
    String code = "let name: string = \"value\"; let number: number = 42;";
    Parser parser = getParser(code);
    ReworkedInterpreter reworkedInterpreter =
        ReworkedInterpreterFactory.buildInterpreter("1.0", new OutputMock());

    while (parser.hasNext()) {
      reworkedInterpreter = reworkedInterpreter.interpret(parser.next());
    }

    InterpreterVisitorV3 visitor = (InterpreterVisitorV3) reworkedInterpreter.getVisitor();
    VariablesRepository repository = visitor.getVariablesRepository();
    assertEquals("value", repository.getNewVariable(new VariableIdentifier("name")).value());
    Assertions.assertEquals(
        42, repository.getNewVariable(new VariableIdentifier("number")).value());
  }

  @Test
  public void testInitializedVar() {
    String code = "let pi: number;\n" + "pi = 3.14;\n" + "println(pi / 2);";
    Parser parser = getParser(code);
    ReworkedInterpreter reworkedInterpreter =
        ReworkedInterpreterFactory.buildInterpreter("1.0", new OutputMock());

    while (parser.hasNext()) {
      reworkedInterpreter = reworkedInterpreter.interpret(parser.next());
    }

    InterpreterVisitorV3 visitor = (InterpreterVisitorV3) reworkedInterpreter.getVisitor();
    VariablesRepository repository = visitor.getVariablesRepository();
    assertEquals(3.14, repository.getNewVariable(new VariableIdentifier("pi")).value());
  }

  @Test
  public void constVarTest() {
    String code = "let pi: number = \"hola\";";
    Parser parser = getParser(code);
    ReworkedInterpreter reworkedInterpreter =
        ReworkedInterpreterFactory.buildInterpreter("1.0", new OutputMock());

    assertThrows(
        MismatchTypeException.class,
        () -> {
          while (parser.hasNext()) {
            reworkedInterpreter.interpret(parser.next());
          }
        });
  }
}
