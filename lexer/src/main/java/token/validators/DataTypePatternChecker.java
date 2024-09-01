package token.validators;

import java.util.Map;
import token.types.TokenType;

public class DataTypePatternChecker implements TypeGetter {
  private final Map<String, TokenType> dataTypes;

  public DataTypePatternChecker(Map<String, TokenType> dataTypes) {
    this.dataTypes = dataTypes;
  }

  @Override
  public TokenType getType(String word) {
    return dataTypes.getOrDefault(word, null);
  }
}
