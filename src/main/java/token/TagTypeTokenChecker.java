package token;

import java.util.HashMap;
import java.util.Map;

public class TagTypeTokenChecker implements TypeGetter{

    private static final Map<String, TokenType> reservedWords;

    static {
        reservedWords = new HashMap<>();
        reservedWords.put("final", TokenType.FINAL_KEYWORD);
        reservedWords.put("public", TokenType.PUBLIC_KEYWORD);
        reservedWords.put("String", TokenType.STRING_TYPE);
        reservedWords.put("int", TokenType.INTEGER_TYPE);
        reservedWords.put("float", TokenType.FLOAT_TYPE);
        reservedWords.put("boolean", TokenType.BOOLEAN_TYPE);
        reservedWords.put("=", TokenType.ASSIGNATION);
        reservedWords.put(";", TokenType.ASSIGNATION);
    }


    @Override
    public TokenType getType(String word) {
        return reservedWords.getOrDefault(word, null);
    }
}
