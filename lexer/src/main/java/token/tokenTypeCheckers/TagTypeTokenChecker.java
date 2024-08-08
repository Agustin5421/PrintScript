package token.tokenTypeCheckers;

import token.tokenTypes.TokenDataType;
import token.tokenTypes.TokenTagType;
import token.tokenTypes.TokenType;

import java.util.HashMap;
import java.util.Map;

public class TagTypeTokenChecker implements TypeGetter{

    private static final Map<String, TokenType> reservedWords;

    static {
        reservedWords = new HashMap<>();

        //reservedWords.put("final", TokenTagType.FINAL_KEYWORD);
        //reservedWords.put("public", TokenTagType.PUBLIC_KEYWORD);
        reservedWords.put("string", TokenDataType.STRING_TYPE);
        reservedWords.put("number", TokenDataType.NUMBER_TYPE);
        //reservedWords.put("float", TokenDataType.FLOAT_TYPE);
        //reservedWords.put("boolean", TokenDataType.BOOLEAN_TYPE);
        reservedWords.put("=", TokenTagType.ASSIGNATION);
        reservedWords.put(";", TokenTagType.SEMICOLON);
        reservedWords.put("let", TokenTagType.DECLARATION);
        reservedWords.put(":", TokenTagType.SYNTAX);
    }


    @Override
    public TokenType getType(String word) {
        return reservedWords.getOrDefault(word, null);
    }
}
