package lexer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MyNewLexerTest {

  @Test
  public void readInputStream() throws IOException {
    String input = "Hello! this is a String";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    MyNewLexer lexer = new MyNewLexer(inputStream);

    List<String> expectedTokens = List.of("Hello", "!", "this", "is", "a", "String");

    List<String> actualTokens = new ArrayList<>();

    while (lexer.hasNext()) {
      actualTokens.add(lexer.next());
    }

    Assertions.assertEquals(expectedTokens, actualTokens);
    System.out.println(actualTokens);
  }

  @Test
  public void readText() throws IOException {
    String input = "This is a binary expression: 5+3";
    InputStream inputStream = new ByteArrayInputStream(input.getBytes());
    MyNewLexer lexer = new MyNewLexer(inputStream);

    List<String> expectedTokens =
        List.of("This", "is", "a", "binary", "expression", ":", "5", "+", "3");

    List<String> actualTokens = new ArrayList<>();

    while (lexer.hasNext()) {
      actualTokens.add(lexer.next());
    }

    // Comparamos los tokens obtenidos con los tokens esperados
    Assertions.assertEquals(expectedTokens, actualTokens);
    System.out.println(actualTokens);
  }
}
