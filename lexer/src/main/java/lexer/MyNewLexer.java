package lexer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.regex.Pattern;

public class MyNewLexer implements Iterator<String> {
  private final InputStream inputStream;
  private final Pattern pattern =
      Pattern.compile(
          "\"[^\"]*\"" // Text between double quotes
              + "|'[^']*'" // Text between single quotes
              + "|\\d+(\\.\\d+)?[a-zA-Z_]*" // Numbers with optional decimal part and units (with
              // letters)
              + "|\\b[a-zA-Z_][a-zA-Z\\d_]*\\b" // Identifiers and keywords
              + "|[=;:]" // Equal, semicolon, and colon
              + "|[+\\-*/%]" // Arithmetic operands
              + "|[()<>{},]" // Parentheses, angle brackets, curly braces, comma
              + "|[.]" // Dot
              + "|\\S" // Any other single character (mismatch), excluding spaces
          );


  public MyNewLexer(InputStream inputStream) throws IOException {
    this.inputStream = inputStream;
  }

  public String newTokenize() throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    int byteRead;

    while ((byteRead = inputStream.read()) != -1) {
      char tempChar = (char) byteRead;
      StringBuilder temp = new StringBuilder(stringBuilder);

      temp.append(tempChar);
      if (tempChar == ' ') {
        return stringBuilder.toString();

      } else if (!temp.toString().matches(pattern.pattern())) {
        return stringBuilder.toString();

      } else {
        stringBuilder = temp;
      }
    }

    throw new IllegalStateException("No more tokens");
  }

  @Override
  public boolean hasNext() {
    try {
      return inputStream.available() > 0;
    } catch (IOException e) {
      return false;
    }
  }

  @Override
  public String next() {
    if (!hasNext()) {
      throw new IllegalStateException("No more tokens");
    }
    try {
      return newTokenize();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
