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
import token.types.TokenTagType;
import token.types.TokenType;
import token.validators.TokenTypeChecker;

public class Lexer implements Progressable {
  private final TokenTypeChecker tokenTypeGetter;
  private final List<Observer> observers;
  private int totalLength;

  public Lexer(TokenTypeChecker tokenTypeGetter, List<Observer> observers) {
    this.tokenTypeGetter = tokenTypeGetter;
    this.observers = observers;
  }

  public Lexer(TokenTypeChecker tokenTypeGetter) {
    this.tokenTypeGetter = tokenTypeGetter;
    observers = List.of();
  }

  private static final String TEXT_PATTERNS =
      "\"[^\"]*\""
          + // Text between double quotes
          "|'[^']*'"
          + // Text between single quotes
          "|\\d+(\\.\\d+)?[a-zA-Z_]*"
          + // Numbers with optional decimal part and units (with letters)
          "|\\b[a-zA-Z_][a-zA-Z\\d_]*\\b"
          + // Identifiers and keywords
          "|[=;:]"
          + // Equal, semicolon, and colon
          "|[+\\-*/%]"
          + // Arithmetic operands
          "|[()<>{},]"
          + // Parentheses, angle brackets, curly braces, comma
          "|[.]"
          + // Period (for decimal for decimal points or standalone)
          "|\\S"; // Any other single character (mismatch), excluding spaces

  public List<Token> extractTokens(String code) {
    List<Token> tokens = new ArrayList<>();
    Pattern pattern = Pattern.compile(TEXT_PATTERNS);
    Matcher matcher = pattern.matcher(code);

    Position initialPosition = new Position(1, 1);
    int currentIndex = 0;

    List<String> words = new ArrayList<>();
    List<int[]> positions = new ArrayList<>();

    while (matcher.find()) {
      String word = matcher.group();
      words.add(word);
      positions.add(new int[] {matcher.start(), matcher.end()});
    }

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

  // Progress should be calculated over the total words in the code
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
