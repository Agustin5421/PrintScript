package lexer;

import static exceptions.ExceptionMessageBuilder.getExceptionMessage;

import exceptions.UnsupportedCharacter;
import java.util.ArrayList;
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
  private final String code;
  private Position currentPosition;
  private int currentIndex;
  private final int totalLength;

  public Lexer(String code, TokenTypeGetter tokenTypeGetter) {
    this.code = code;
    this.tokenTypeGetter = tokenTypeGetter;
    this.observers = List.of();
    this.pattern = new PatternProvider().getPattern();
    resetMatcher();
    this.currentPosition = new Position(1, 1);
    this.currentIndex = 0;
    this.totalLength = code.length();
  }

  public Lexer(String code, Lexer lexer) {
    this.code = code;
    this.tokenTypeGetter = lexer.tokenTypeGetter;
    this.observers = List.of();
    this.pattern = lexer.pattern;
    resetMatcher();
    this.currentPosition = new Position(1, 1);
    this.currentIndex = 0;
    this.totalLength = code.length();
  }

  private void resetMatcher() {
    this.matcher = pattern.matcher(code);
  }

  public List<Token> tokenize(String code) {
    Lexer lexer = new Lexer(code, this);

    List<Token> tokens = new ArrayList<>();

    while (lexer.hasNext()) {
      tokens.add(lexer.next());
    }
    return tokens;
  }

  public Lexer setInput(String code) {
    return new Lexer(code, this);
  }

  @Override
  public boolean hasNext() {
    matcher.region(currentIndex, totalLength);
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

    currentPosition = updatePosition(code, currentIndex, start, currentPosition);
    Position finalPosition = updatePosition(code, start, end, currentPosition);

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

  private Position updatePosition(
      String code, int initialIndex, int finalIndex, Position position) {
    int row = position.row();
    int col = position.col();

    for (int i = initialIndex; i < finalIndex; i++) {
      if (code.charAt(i) == '\n') {
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
    return (((float) currentIndex / totalLength) * 100);
  }

  @Override
  public void notifyObservers() {
    for (Observer observer : observers) {
      observer.update(this);
    }
  }
}
