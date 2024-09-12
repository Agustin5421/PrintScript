package lexer;

import exceptions.UnsupportedCharacter;
import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import token.Position;
import token.Token;
import token.types.TokenSyntaxType;
import token.types.TokenType;
import token.validators.TokenTypeGetter;

public class Lexer implements Iterator<Token> {
  private final TokenTypeGetter tokenTypeGetter;
  private final Pattern pattern;
  private Matcher matcher;
  private final BufferedReader reader;
  private String currentLine;
  private Position currentPosition;
  private final Queue<Token> tokens; // Token buffer

  public Lexer(InputStream inputStream, TokenTypeGetter tokenTypeGetter) throws IOException {
    this.reader = new BufferedReader(new InputStreamReader(inputStream));
    this.tokenTypeGetter = tokenTypeGetter;
    this.pattern = new PatternProvider().getPattern();
    this.currentPosition = new Position(1, 1);
    this.tokens = new LinkedList<>();
    advanceToNextLine();
  }

  private void advanceToNextLine() throws IOException {
    currentLine = reader.readLine();
    if (currentLine != null) {
      matcher = pattern.matcher(currentLine);
    }
  }

  @Override
  public boolean hasNext() {
    // If there are tokens in the buffer, return true
    if (!tokens.isEmpty()) {
      return true;
    }

    // Once there are no more tokens in the buffer, tokenize the next line
    try {
        while (matcher.find()) {
          String word = matcher.group();
          int start = matcher.start();
          int end = matcher.end();

          // Update the current position
          Position startPosition = new Position(currentPosition.row(), start);
          Position endPosition = updatePosition(start, end, startPosition);

          currentPosition = endPosition;
          currentPosition = updatePosition(end, currentLine.length(), currentPosition);

          // Get token type and create a new token
          TokenType type = tokenTypeGetter.getType(word);
          Token token = new Token(type, word, startPosition, endPosition);

          if (token.nodeType() == TokenSyntaxType.INVALID) {
            throw new UnsupportedCharacter("Invalid token: " + token.value());
          }

          tokens.add(token);
        }
        advanceToNextLine(); // Read the next line once we have processed the current one
    } catch (IOException e) {
      return false;
    }

    return !tokens.isEmpty();
  }

  @Override
  public Token next() {
    if (!hasNext()) {
      throw new IllegalStateException("No more tokens available");
    }
    return tokens.poll();
  }

  private Position updatePosition(int startIndex, int endIndex, Position position) {
    int row = position.row();
    int col = position.col();

    for (int i = startIndex; i < endIndex; i++) {
      if (currentLine.charAt(i) == '\n') {
        row++;
        col = 1;
      } else {
        col++;
      }
    }

    return new Position(row, col);
  }

  public Token peek() {
    if (!hasNext()) {
      return null;
    }

    return tokens.peek();
  }

  public Lexer setInput(InputStream inputStream) throws IOException {
    return new Lexer(inputStream, this.tokenTypeGetter);
  }

  public Lexer setInputAsString(String code) {
    try {
      InputStream myCode = new ByteArrayInputStream(code.getBytes());
      return new Lexer(myCode, this.tokenTypeGetter);
    } catch (IOException e) {
      return null;
    }
  }
}
