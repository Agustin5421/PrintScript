package token.validators;

import java.util.Map;
import token.types.TokenType;

public class OperandPatternChecker implements TypeGetter {
  private final Map<String, TokenType> operationsMap;

  public OperandPatternChecker(Map<String, TokenType> operationsMap) {
    this.operationsMap = operationsMap;
  }

  @Override
  public TokenType getType(String word) {
    return operationsMap.getOrDefault(word, null);
  }
}
