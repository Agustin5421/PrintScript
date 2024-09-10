package lexer;

import static exceptions.ExceptionMessageBuilder.getExceptionMessage;

import exceptions.UnsupportedCharacter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import observers.Observer;
import observers.Progressable;
import token.Position;
import token.Token;
import token.types.TokenSyntaxType;
import token.types.TokenType;
import token.validators.TokenTypeGetter;

public class Lexer implements Progressable, Iterator<Token> {
  private final TokenTypeGetter tokenTypeGetter;
  private final List<Observer> observers;
  private final Pattern pattern;
  private Matcher matcher;
  private final InputStream inputStream;
  private Position currentPosition;
  private int currentIndex;
  private final StringBuilder currentWordBuilder = new StringBuilder();

  public Lexer(InputStream inputStream, TokenTypeGetter tokenTypeGetter) throws IOException {
    this.inputStream = inputStream;
    this.tokenTypeGetter = tokenTypeGetter;
    this.observers = List.of();
    this.pattern = new PatternProvider().getPattern();
    this.currentPosition = new Position(1, 1);
    this.currentIndex = 0;
    resetMatcher();
  }

  private void resetMatcher() throws IOException {
    String content = readInputStream();
    this.matcher = pattern.matcher(content);
  }

  private String readInputStream() throws IOException {
    StringBuilder content = new StringBuilder();
    int byteRead;
    while ((byteRead = inputStream.read()) != -1) {
      content.append((char) byteRead);
    }
    return content.toString();
  }

  @Override
  public boolean hasNext() {
    matcher.region(currentIndex, matcher.regionEnd());
    return matcher.find();
  }

  @Override
  public Token next() {
    if (!hasNext()) {
      throw new IllegalStateException("No more tokens available");
    }

    String word = matcher.group();
    int start = matcher.start();
    int end = matcher.end();

    currentPosition = updatePosition(start, end, currentPosition);
    Position finalPosition = updatePosition(start, end, currentPosition);

    currentIndex = end;

    TokenType type = tokenTypeGetter.getType(word);
    Token token = new Token(type, word, currentPosition, finalPosition);

    currentPosition = finalPosition;

    if (token.nodeType() == TokenSyntaxType.INVALID) {
      String message =
          getExceptionMessage(token.value(), currentPosition.row(), currentPosition.col());
      throw new UnsupportedCharacter(message);
    }

    updateProgress();
    return token;
  }

  private Position updatePosition(int startIndex, int endIndex, Position position) {
    int row = position.row();
    int col = position.col();

    for (int i = startIndex; i < endIndex; i++) {
      if (currentWordBuilder.charAt(i) == '\n') {
        row++;
        col = 1;
      } else {
        col++;
      }
    }

    return new Position(row, col);
  }

  private void updateProgress() {
    notifyObservers();
  }

  @Override
  public float getProgress() {
    return (((float) currentIndex / currentWordBuilder.length()) * 100);
  }

  @Override
  public void notifyObservers() {
    for (Observer observer : observers) {
      observer.update(this);
    }
  }

  public Token peek() {
    if (!hasNext()) {
      throw new IllegalStateException("No more tokens available");
    }

    String word = matcher.group();
    int start = matcher.start();
    int end = matcher.end();

    Position finalPosition = updatePosition(start, end, currentPosition);

    TokenType type = tokenTypeGetter.getType(word);

    return new Token(type, word, currentPosition, finalPosition);
  }

  public Lexer setInput(InputStream inputStream) throws IOException {
    return new Lexer(inputStream, this.tokenTypeGetter);
  }
}
