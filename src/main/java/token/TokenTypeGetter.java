package token;

import java.util.List;

public class TokenTypeGetter {

    private final List<TypeGetter> validators;

    public TokenTypeGetter(List<TypeGetter> validators) {
        this.validators = validators;
    }


    public TokenType getType(String token) {
        for (TypeGetter validator : validators) {
            if (validator.checkType(token)) {
                return validator.getType();
            }
        }
        //TODO: should it just throw an exception instead of a MISMATCH type?
        return TokenType.MISMATCH;
    }
}
