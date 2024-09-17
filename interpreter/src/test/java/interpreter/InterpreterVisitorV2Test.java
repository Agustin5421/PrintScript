package interpreter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ast.identifier.Identifier;
import factory.LexerFactory;
import factory.ParserFactory;
import interpreter.factory.InterpreterFactory;
import interpreter.factory.ReworkedInterpreterFactory;
import interpreter.visitor.InterpreterVisitorV3;
import interpreter.visitor.repository.VariableIdentifier;
import interpreter.visitor.repository.VariableIdentifierFactory;
import interpreter.visitor.repository.VariablesRepository;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.List;

import interpreter.visitor.staticprovider.Inputs;
import lexer.Lexer;
import org.junit.jupiter.api.Test;
import output.OutputListString;
import output.OutputMock;
import parsers.Parser;
import token.Position;

public class InterpreterVisitorV2Test {
  private final List<String> prints = List.of();

  private Parser getParser(String code) {
    Lexer lexer = LexerFactory.getLexer("1.1");
    try {
      lexer = lexer.setInput(new ByteArrayInputStream(code.getBytes()));
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
    Inputs.setInputs(new ArrayDeque<>(List.of(input)));

    Parser parser = getParser(code);
    ReworkedInterpreter reworkedInterpreter =
        ReworkedInterpreterFactory.buildInterpreter("1.1", new OutputMock());

    while (parser.hasNext()) {
      reworkedInterpreter = reworkedInterpreter.interpret(parser.next());
    }

    InterpreterVisitorV3 visitor = (InterpreterVisitorV3) reworkedInterpreter.getVisitor();
    VariablesRepository repository = visitor.getVariablesRepository();

    assertEquals(
        42.0,
        repository
            .getNewVariable(new VariableIdentifier("x"))
            .value());
    Inputs.setInputs(null);
  }

  @Test
  public void testReadInputNumberInt() {
    String code = "let x: number = readInput(\"Name:\");";

    String input = "42";
    Inputs.setInputs(null);
    Inputs.setInputs(new ArrayDeque<>(List.of(input)));

    Parser parser = getParser(code);
    ReworkedInterpreter reworkedInterpreter =
        ReworkedInterpreterFactory.buildInterpreter("1.1", new OutputMock());

    while (parser.hasNext()) {
      reworkedInterpreter = reworkedInterpreter.interpret(parser.next());
    }

    InterpreterVisitorV3 visitor = (InterpreterVisitorV3) reworkedInterpreter.getVisitor();
    VariablesRepository repository = visitor.getVariablesRepository();

    assertEquals(
        42,
        repository
            .getNewVariable(new VariableIdentifier("x"))
            .value());

    Inputs.setInputs(null);
  }

  @Test
  public void testReadInputString() {
    String code = "let x: string = readInput(\"Name:\");";
    Parser parser = getParser(code);
    ReworkedInterpreter reworkedInterpreter =
        ReworkedInterpreterFactory.buildInterpreter("1.1", new OutputMock());

    String input = "Hello, world!";
    Inputs.setInputs(null);
    Inputs.setInputs(new ArrayDeque<>(List.of(input)));

    while (parser.hasNext()) {
      reworkedInterpreter = reworkedInterpreter.interpret(parser.next());
    }

    InterpreterVisitorV3 visitor = (InterpreterVisitorV3) reworkedInterpreter.getVisitor();
    VariablesRepository repository = visitor.getVariablesRepository();

    assertEquals(
        "Hello, world!",
        repository
            .getNewVariable(new VariableIdentifier("x"))
            .value());

    Inputs.setInputs(null);
  }

  @Test
  public void testReadEnvNonExistent() {
    String code = "readEnv(\"NON_EXISTENT_ENV_VAR\");";
    Parser parser = getParser(code);
    ReworkedInterpreter reworkedInterpreter =
        ReworkedInterpreterFactory.buildInterpreter("1.1", new OutputMock());

    assertThrows(
        RuntimeException.class,
        () -> {
          while (parser.hasNext()) {
            reworkedInterpreter.interpret(parser.next());
          }
        });
  }

  @Test
  public void testReadInputEmpty() {
    String code = "let x:string =readInput(\"Name:\");";

    String input = "hola";
    Inputs.setInputs(null);
    Inputs.setInputs(new ArrayDeque<>(List.of(input)));

    Parser parser = getParser(code);
    ReworkedInterpreter reworkedInterpreter =
        ReworkedInterpreterFactory.buildInterpreter("1.1", new OutputMock());

    while (parser.hasNext()) {
      reworkedInterpreter = reworkedInterpreter.interpret(parser.next());
    }

    InterpreterVisitorV3 visitor = (InterpreterVisitorV3) reworkedInterpreter.getVisitor();
    VariablesRepository repository = visitor.getVariablesRepository();

    assertEquals(
        "hola",
        repository
            .getNewVariable(new VariableIdentifier("x"))
            .value());
    Inputs.setInputs(null);
  }

  @Test
  public void testReadInputBooleanTrue() {
    String code = "let x:boolean = readInput(\"Name:\");";

    String input = "true";
    Inputs.setInputs(null);
    Inputs.setInputs(new ArrayDeque<>(List.of(input)));

    Parser parser = getParser(code);
    ReworkedInterpreter reworkedInterpreter =
        ReworkedInterpreterFactory.buildInterpreter("1.1", new OutputMock());

    while (parser.hasNext()) {
      reworkedInterpreter = reworkedInterpreter.interpret(parser.next());
    }

    InterpreterVisitorV3 visitor = (InterpreterVisitorV3) reworkedInterpreter.getVisitor();
    VariablesRepository repository = visitor.getVariablesRepository();

    assertEquals(
        true,
        repository
            .getNewVariable(new VariableIdentifier("x"))
            .value());
    Inputs.setInputs(null);
  }

  @Test
  public void testPrintList() {
    String code =
        "const booleanValue: boolean = true;\n"
            + "if(booleanValue) {\n"
            + "    println(\"if statement working correctly\");\n"
            + "}\n"
            + "println(\"outside of conditional\");\n";
    Parser parser = getParser(code);
    ReworkedInterpreter reworkedInterpreter =
        ReworkedInterpreterFactory.buildInterpreter("1.1", new OutputListString());

    while (parser.hasNext()) {
      reworkedInterpreter = reworkedInterpreter.interpret(parser.next());
    }

    InterpreterVisitorV3 visitor = (InterpreterVisitorV3) reworkedInterpreter.getVisitor();
    OutputListString output = (OutputListString) visitor.getOutputResult();

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
    ReworkedInterpreter reworkedInterpreter =
        ReworkedInterpreterFactory.buildInterpreter("1.1", new OutputListString());

    while (parser.hasNext()) {
      reworkedInterpreter = reworkedInterpreter.interpret(parser.next());
    }

    InterpreterVisitorV3 visitor = (InterpreterVisitorV3) reworkedInterpreter.getVisitor();
    OutputListString output = (OutputListString) visitor.getOutputResult();

    assertEquals("Hello", output.getSavedResults().get(0));
    assertEquals("World", output.getSavedResults().get(1));
  }

  @Test
  public void testReadEnvExistingVariable() {
    String code = "let x: string = readEnv(\"UNIVERSAL_CONSTANT\");";

    Parser parser = getParser(code);
    ReworkedInterpreter reworkedInterpreter =
        ReworkedInterpreterFactory.buildInterpreter("1.1", new OutputListString());

    while (parser.hasNext()) {
      reworkedInterpreter = reworkedInterpreter.interpret(parser.next());
    }

    InterpreterVisitorV3 visitor = (InterpreterVisitorV3) reworkedInterpreter.getVisitor();
    VariablesRepository repository = visitor.getVariablesRepository();

    assertEquals("constant", repository.getNewVariable(new VariableIdentifier("x")).value());
  }

  @Test
  public void testReadEnvExistingBooleanVariable() {
    String code = "let x: boolean = readEnv(\"IS_CONSTANT\");";

    Parser parser = getParser(code);
    ReworkedInterpreter reworkedInterpreter =
        ReworkedInterpreterFactory.buildInterpreter("1.1", new OutputListString());

    while (parser.hasNext()) {
      reworkedInterpreter = reworkedInterpreter.interpret(parser.next());
    }

    InterpreterVisitorV3 visitor = (InterpreterVisitorV3) reworkedInterpreter.getVisitor();
    VariablesRepository repository = visitor.getVariablesRepository();

    assertEquals(true, repository.getNewVariable(new VariableIdentifier("x")).value());
  }

  @Test
  public void testReadEnvExistingIntegerVariable() {
    String code = "let x: number = readEnv(\"GRAVITY\");";

    Parser parser = getParser(code);
    ReworkedInterpreter reworkedInterpreter =
        ReworkedInterpreterFactory.buildInterpreter("1.1", new OutputListString());

    while (parser.hasNext()) {
      reworkedInterpreter = reworkedInterpreter.interpret(parser.next());
    }

    InterpreterVisitorV3 visitor = (InterpreterVisitorV3) reworkedInterpreter.getVisitor();
    VariablesRepository repository = visitor.getVariablesRepository();

    assertEquals(9.81, repository.getNewVariable(new VariableIdentifier("x")).value());
  }

  @Test
  public void testReadEnvExistingDoubleVariable() {
    String code = "let x: number = readEnv(\"PLANCK_CONSTANT\");";

    Parser parser = getParser(code);
    ReworkedInterpreter reworkedInterpreter =
        ReworkedInterpreterFactory.buildInterpreter("1.1", new OutputListString());

    while (parser.hasNext()) {
      reworkedInterpreter = reworkedInterpreter.interpret(parser.next());
    }

    InterpreterVisitorV3 visitor = (InterpreterVisitorV3) reworkedInterpreter.getVisitor();
    VariablesRepository repository = visitor.getVariablesRepository();

    assertEquals(6.62607015e-34, repository.getNewVariable(new VariableIdentifier("x")).value());
  }
}
