package token;

import java.util.List;

public class TokenTypeGetter {

    private final List<TypeGetter> validators;

    public TokenTypeGetter(List<TypeGetter> validators) {
        this.validators = validators;
    }


    public TokenType getType(String word) {
        for (TypeGetter validator : validators) {
            TokenType type = validator.getType(word);
            if (type != null) {
                return type;
            }
        }
        //TODO: every word that does not match is an identifier?
        return TokenType.IDENTIFIER;
    }
}
