package lexer;

import static exceptions.ExceptionMessageBuilder.getExceptionMessage;

import exceptions.UnsupportedCharacter;
import java.util.ArrayList;
import java.util.List;
import observers.Observer;
import observers.Progressable;
import token.Position;
import token.Token;
import token.types.TokenTagType;
import token.types.TokenType;
import token.validators.TokenTypeChecker;

public class Lexer implements Progressable {
  private final TokenTypeChecker tokenTypeGetter;
  private final PatternHandler patternHandler;
  private final List<Observer> observers;
  private int totalLength;

  public Lexer(TokenTypeChecker tokenTypeGetter) {
    this.tokenTypeGetter = tokenTypeGetter;
    this.patternHandler = new PatternHandler();
    this.observers = List.of();
  }

  public Lexer(TokenTypeChecker tokenTypeGetter, List<Observer> observers) {
    this.tokenTypeGetter = tokenTypeGetter;
    this.patternHandler = new PatternHandler();
    this.observers = List.of();
  }

  public Lexer(
          TokenTypeChecker tokenTypeGetter, List<Observer> observers, PatternHandler patternHandler) {
    this.tokenTypeGetter = tokenTypeGetter;
    this.observers = observers;
    this.patternHandler = patternHandler;
  }

  public Lexer(TokenTypeChecker tokenTypeGetter, PatternHandler patternHandler) {
    this.tokenTypeGetter = tokenTypeGetter;
    this.patternHandler = patternHandler;
    this.observers = List.of();
  }

  public List<Token> extractTokens(String code) {
    List<Token> tokens = new ArrayList<>();
    List<String> words = patternHandler.extractWords(code);
    List<int[]> positions = patternHandler.extractPositions(code);

    Position initialPosition = new Position(1, 1);
    int currentIndex = 0;

    totalLength = words.size();

    for (int i = 0; i < words.size(); i++) {
      String word = words.get(i);
      int start = positions.get(i)[0];
      int end = positions.get(i)[1];

      initialPosition = updatePosition(code, currentIndex, start, initialPosition);
      Position finalPosition = updatePosition(code, start, end, initialPosition);

      currentIndex = end;

      TokenType type = tokenTypeGetter.getType(word);
      Token token = new Token(type, word, initialPosition, finalPosition);
      tokens.add(token);

      initialPosition = finalPosition;

      updateProgress();
    }

    validateTokens(tokens);

    return tokens;
  }

  private static void validateTokens(List<Token> tokens) {
    for (Token token : tokens) {
      if (token.getType() == TokenTagType.INVALID) {
        Position position = token.getInitialPosition();
        String message = getExceptionMessage(token.getValue(), position.row(), position.col());
        throw new UnsupportedCharacter(message);
      }
    }
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
    return (((float) 1 / totalLength) * 100);
  }

  @Override
  public void addObserver(Observer observer) {
    observers.add(observer);
  }

  @Override
  public void removeObserver(Observer observer) {
    observers.remove(observer);
  }

  @Override
  public void notifyObservers() {
    for (Observer observer : observers) {
      observer.update(this);
    }
  }
}
