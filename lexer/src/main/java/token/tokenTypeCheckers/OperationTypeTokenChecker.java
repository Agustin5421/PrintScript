package token.tokenTypeCheckers;

import java.util.HashMap;
import java.util.Map;
import token.tokenTypes.TokenDataType;
import token.tokenTypes.TokenType;

public class OperationTypeTokenChecker implements TypeGetter {
  private static final Map<String, TokenType> operationsMap;

  static {
    operationsMap = new HashMap<>();
    operationsMap.put("+", TokenDataType.OPERAND);
    operationsMap.put("-", TokenDataType.OPERAND);
    operationsMap.put("*", TokenDataType.OPERAND);
    operationsMap.put("/", TokenDataType.OPERAND);
    operationsMap.put("<", TokenDataType.OPERAND);
    operationsMap.put(">", TokenDataType.OPERAND);
  }

  @Override
  public TokenType getType(String word) {
    return operationsMap.getOrDefault(word, null);
  }
}
