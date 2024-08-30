package token.validators;

import java.util.HashMap;
import java.util.Map;
import token.types.TokenDataType;
import token.types.TokenTagType;
import token.types.TokenType;

public class SyntaxTokenChecker implements TypeGetter {

  private static final Map<String, TokenType> reservedWords;

  static {
    reservedWords = new HashMap<>();
    reservedWords.put("string", TokenDataType.STRING_TYPE);
    reservedWords.put("number", TokenDataType.NUMBER_TYPE);
    reservedWords.put("=", TokenTagType.ASSIGNATION);
    reservedWords.put(";", TokenTagType.SEMICOLON);
    reservedWords.put("let", TokenTagType.DECLARATION);
    reservedWords.put(":", TokenTagType.COLON);
    reservedWords.put("(", TokenTagType.OPEN_PARENTHESIS);
    reservedWords.put(")", TokenTagType.CLOSE_PARENTHESIS);
    reservedWords.put(",", TokenTagType.COMMA);
    reservedWords.put("if", TokenTagType.IF);
    reservedWords.put("{", TokenTagType.OPEN_BRACES);
    reservedWords.put("}", TokenTagType.CLOSE_BRACES);
  }

  @Override
  public TokenType getType(String word) {
    return reservedWords.getOrDefault(word, null);
  }
}
