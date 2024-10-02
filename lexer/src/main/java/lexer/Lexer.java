package lexer;

import exceptions.UnsupportedCharacter;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import observers.Observable;
import observers.ProgressObserver;
import position.Position;
import token.Token;
import token.types.TokenSyntaxType;
import token.types.TokenType;
import token.validators.TokenTypeGetter;

public class Lexer implements Iterator<Token>, Observable {
  private final TokenTypeGetter tokenTypeGetter;
  private final Pattern pattern;
  private Matcher matcher;
  private final BufferedReader reader;
  private String currentLine;
  private Position currentPosition;
  private final Queue<Token> tokens; // Token buffer
  private final ProgressObserver observer;
  private final int length;
  private int bytesRead = 0;

  public Lexer(InputStream inputStream, TokenTypeGetter tokenTypeGetter) throws IOException {
    length = inputStream.available();
    this.reader = new BufferedReader(new InputStreamReader(inputStream));
    this.tokenTypeGetter = tokenTypeGetter;
    this.pattern = new PatternProvider().getPattern();
    this.currentPosition = new Position(1, 1);
    this.tokens = new LinkedList<>();
    this.observer = null;
    advanceToNextLine();
  }

  public Lexer(InputStream inputStream, TokenTypeGetter tokenTypeGetter, ProgressObserver observer)
      throws IOException {
    length = inputStream.available();
    this.reader = new BufferedReader(new InputStreamReader(inputStream));
    this.tokenTypeGetter = tokenTypeGetter;
    this.pattern = new PatternProvider().getPattern();
    this.currentPosition = new Position(1, 1);
    this.tokens = new LinkedList<>();
    this.observer = observer;
    advanceToNextLine();
  }

  private void advanceToNextLine() throws IOException {
    while (currentLine == null || !matcher.find()) {
      currentLine = reader.readLine();
      currentPosition = new Position(currentPosition.row() + 1, 1);

      if (currentLine == null) {
        break;
      }

      bytesRead += currentLine.getBytes().length + 1;

      matcher = pattern.matcher(currentLine);
    }
    if (currentLine != null) {
      matcher = pattern.matcher(currentLine);
    }
  }

  @Override
  public boolean hasNext() {
    // If there are tokens in the buffer or there is a line to tokenize, return true
    if (!tokens.isEmpty() || currentLine != null) {
      return true;
    }

    if (observer != null) {
      observer.finish();
    }
    return false;
  }

  private void tokenizeLine() {
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

      if (token.tokenType() == TokenSyntaxType.INVALID) {
        throw new UnsupportedCharacter("Invalid token: " + token.value());
      }
      tokens.add(token);
    }
    try {
      advanceToNextLine();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Token next() {
    if (tokens.isEmpty()) {
      tokenizeLine();
    }

    notifyObservers();
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
    return new Lexer(inputStream, this.tokenTypeGetter, this.observer);
  }

  public Lexer setInputAsString(String code) {
    try {
      InputStream myCode = new ByteArrayInputStream(code.getBytes());
      return new Lexer(myCode, this.tokenTypeGetter, this.observer);
    } catch (IOException e) {
      return null;
    }
  }

  @Override
  public void notifyObservers() {
    if (observer != null) {
      observer.update(this);
    }
  }

  @Override
  public float getProgress() {
    return ((float) length / bytesRead);
  }

  public Lexer setInputWithObserver(InputStream input, ProgressObserver observer)
      throws IOException {
    return new Lexer(input, this.tokenTypeGetter, observer);
  }
}
