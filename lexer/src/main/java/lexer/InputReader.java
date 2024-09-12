package lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InputReader {
  private final BufferedReader reader;

  public InputReader(InputStream inputStream) {
    this.reader = new BufferedReader(new InputStreamReader(inputStream));
  }

  /**
   * Reads the next line from the input stream.
   *
   * @return the next line or null if end of file is reached
   */
  public String readNextLine() throws IOException {
    return reader.readLine(); // Returns null when EOF is reached
  }
}
