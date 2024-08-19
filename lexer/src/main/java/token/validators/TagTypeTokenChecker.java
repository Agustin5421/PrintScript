package token.validators;

import java.util.HashMap;
import java.util.Map;
import token.types.TokenDataType;
import token.types.TokenTagType;
import token.types.TokenType;

public class TagTypeTokenChecker implements TypeGetter {

  private static final Map<String, TokenType> reservedWords;

  static {
    reservedWords = new HashMap<>();

    // reservedWords.put("final", TokenTagType.FINAL_KEYWORD);
    // reservedWords.put("public", TokenTagType.PUBLIC_KEYWORD);
    reservedWords.put("string", TokenDataType.STRING_TYPE);
    reservedWords.put("number", TokenDataType.NUMBER_TYPE);
    // reservedWords.put("float", TokenDataType.FLOAT_TYPE);
    // reservedWords.put("boolean", TokenDataType.BOOLEAN_TYPE);
    reservedWords.put("=", TokenTagType.ASSIGNATION);
    reservedWords.put(";", TokenTagType.SEMICOLON);
    reservedWords.put("let", TokenTagType.DECLARATION);
    reservedWords.put(":", TokenTagType.SYNTAX);
    reservedWords.put("(", TokenTagType.OPEN_PARENTHESIS);
    reservedWords.put(")", TokenTagType.CLOSE_PARENTHESIS);
    reservedWords.put(",", TokenTagType.COMMA);
  }

  @Override
  public TokenType getType(String word) {
    return reservedWords.getOrDefault(word, null);
  }
}