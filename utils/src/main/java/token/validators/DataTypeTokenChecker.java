package token.validators;

import token.types.TokenType;
import token.types.TokenValueType;

public class DataTypeTokenChecker implements TypeGetter {

  @Override
  public TokenType getType(String value) {
    if (isString(value)) {
      return TokenValueType.STRING;
    } else if (isNumber(value)) {
      return TokenValueType.NUMBER;
    } else {
      return null;
    }
  }

  private static boolean isString(String value) {
    return (value.startsWith("\"") && value.endsWith("\""))
        || (value.startsWith("'") && value.endsWith("'"));
  }

  private static boolean isNumber(String value) {
    return isInteger(value) || isFloat(value);
  }

  private static boolean isInteger(String value) {
    try {
      Integer.parseInt(value);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  private static boolean isFloat(String value) {
    try {
      Float.parseFloat(value);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
