package token.types;

public enum TokenSyntaxType implements TokenType {
  IDENTIFIER,
  ASSIGNATION,
  SEMICOLON,
  DECLARATION,
  COLON,
  OPEN_PARENTHESIS,
  CLOSE_PARENTHESIS,
  OPEN_BRACES,
  CLOSE_BRACES,
  INVALID,
  IF,
  ELSE,
  COMMA,
}
