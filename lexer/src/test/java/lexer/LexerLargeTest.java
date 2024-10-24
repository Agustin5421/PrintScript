package lexer;

import factory.LexerFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import token.Token;

public class LexerLargeTest {

  @Test
  public void testLargeInput() throws IOException {
    String input = "println('Hello World'); \n".repeat(32 * 1024);
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());

    Lexer lexer = LexerFactory.getLexer("1.0");
    assert lexer != null;
    lexer = lexer.setInput(inputStream);

    List<Token> tokens = new ArrayList<>();

    while (lexer.hasNext()) {
      tokens.add(lexer.next());
    }

    System.out.println("Size is: " + tokens.size());
  }
}
