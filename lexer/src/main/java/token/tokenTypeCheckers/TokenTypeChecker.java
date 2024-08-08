package token.tokenTypeCheckers;

import token.tokenTypes.TokenTagType;
import token.tokenTypes.TokenType;

import java.util.List;

public class TokenTypeChecker implements TypeGetter{

    private final List<TypeGetter> validators;

    public TokenTypeChecker(List<TypeGetter> validators) {
        this.validators = validators;
    }


    public TokenType getType(String word) {
        for (TypeGetter validator : validators) {
            TokenType type = validator.getType(word);
            if (type != null) {
                return type;
            }
        }
        return TokenTagType.INVALID;
    }
}
