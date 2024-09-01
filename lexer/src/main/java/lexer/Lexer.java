package lexer;

import static exceptions.ExceptionMessageBuilder.getExceptionMessage;

import exceptions.UnsupportedCharacter;
import java.util.ArrayList;
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

public class Lexer implements Progressable {
  private final TokenTypeGetter tokenTypeGetter;
  private final List<Observer> observers;
  private int totalLength;
  private final PatternProvider patternProvider;

  public Lexer(TokenTypeGetter tokenTypeGetter) {
    this.tokenTypeGetter = tokenTypeGetter;
    observers = List.of();
    patternProvider = new PatternProvider();
  }

  public List<Token> tokenize(String code) {
    List<Token> tokens = new ArrayList<>();
    Pattern pattern = patternProvider.getPattern();
    Matcher matcher = pattern.matcher(code);

    Position initialPosition = new Position(1, 1);
    int currentIndex = 0;

    while (matcher.find()) {
      String word = matcher.group();
      int start = matcher.start();
      int end = matcher.end();

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
      if (token.getType() == TokenSyntaxType.INVALID) {
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

  // Progress should be calculated over the total words in the code
  @Override
  public float getProgress() {
    return (((float) 1 / totalLength) * 100);
  }

  @Override
  public void notifyObservers() {
    for (Observer observer : observers) {
      observer.update(this);
    }
  }
}
