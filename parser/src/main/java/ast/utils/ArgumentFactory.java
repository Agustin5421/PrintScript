package ast.utils;

import ast.expressions.Expression;
import ast.identifier.Identifier;
import ast.literal.LiteralFactory;
import token.Token;
import token.types.TokenTagType;

public class ArgumentFactory {
  public static Expression createArgument(Token token) {
    if (token.getType() == TokenTagType.IDENTIFIER) {
      return new Identifier(token.getValue(), token.getInitialPosition(), token.getFinalPosition());
    } else {
      try {
        return LiteralFactory.createLiteral(token);
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Unsupported argument type");
      }
    }
  }
}
//TODO: delete this?