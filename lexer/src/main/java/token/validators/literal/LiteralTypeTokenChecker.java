package token.validators.literal;

import java.util.List;
import token.types.TokenType;
import token.validators.TypeGetter;

public class LiteralTypeTokenChecker implements TypeGetter {
  private final List<LiteralPatternChecker> literalPatternCheckers;

  public LiteralTypeTokenChecker(List<LiteralPatternChecker> literalPatternCheckers) {
    this.literalPatternCheckers = literalPatternCheckers;
  }

  @Override
  public TokenType getType(String value) {
    for (LiteralPatternChecker literalPatternChecker : literalPatternCheckers) {
      TokenType type = literalPatternChecker.getType(value);
      if (type != null) {
        return type;
      }
    }
    return null;
  }
}
