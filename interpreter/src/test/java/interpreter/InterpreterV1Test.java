package interpreter;

import static org.junit.jupiter.api.Assertions.assertThrows;

import factory.LexerFactory;
import factory.ParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;
import lexer.Lexer;
import org.junit.jupiter.api.Test;
import output.OutputMock;
import output.OutputResult;
import parsers.Parser;

public class InterpreterV1Test extends CommonInterpreterTest {
  private final OutputMock output = new OutputMock();

  @Override
  protected Parser getParser(String code) {
    Lexer lexer = LexerFactory.getLexer("1.0");
    try {
      lexer = Objects.requireNonNull(lexer).setInput(new ByteArrayInputStream(code.getBytes()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Parser parser = ParserFactory.getParser("1.0");
    return parser.setLexer(lexer);
  }

  @Override
  protected Interpreter getInterpreter(OutputResult<String> output) {
    return InterpreterFactory.buildInterpreter("1.0", output);
  }

  @Test
  public void failWithConstantDeclaration() {
    String code = "const x: number = 42;";

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
  public void failWithIfStatement() {
    String code = "if (true) { let x: string = \"value\"; }";

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
  public void failWithBooleanLiteral() {
    String code = "let x: boolean = true";

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
