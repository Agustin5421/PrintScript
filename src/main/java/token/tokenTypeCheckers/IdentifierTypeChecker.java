package token.tokenTypeCheckers;

import token.tokenTypes.TokenTagType;
import token.tokenTypes.TokenType;

import java.util.regex.Pattern;

public class IdentifierTypeChecker implements TypeGetter {

    private static final Pattern JAVA_IDENTIFIER_PATTERN = Pattern.compile("^[a-zA-Z_][a-zA-Z\\d_]*$");

    @Override
    public TokenType getType(String word) {
        if (isValidJavaIdentifier(word)) {
            return TokenTagType.IDENTIFIER;
        }
        return TokenTagType.INVALID;
    }

    private boolean isValidJavaIdentifier(String word) {
        return word != null && !word.isEmpty() && JAVA_IDENTIFIER_PATTERN.matcher(word).matches();
    }
}
