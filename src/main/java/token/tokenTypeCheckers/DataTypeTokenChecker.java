package token.tokenTypeCheckers;

import token.tokenTypes.TokenType;
import token.tokenTypes.TokenValueType;

public class DataTypeTokenChecker implements TypeGetter {

    @Override
    public TokenType getType(String value) {
        if (isString(value)) {
            return TokenValueType.STRING;
        } else if (isNumber(value)) {
            return TokenValueType.NUMBER;
        } /*else if (isFloat(value)) {
            return TokenValueType.FLOAT;
        } else if (isBoolean(value)) {
            return TokenValueType.BOOLEAN;
        } */else {
            return null;
        }
    }

    private static boolean isString(String value) {
        return (value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'"));
    }

    private static boolean isNumber(String value) {
        return isInteger(value) || isFloat(value);
    }

    private static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isFloat(String value) {
        try {
            Float.parseFloat(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /*
    private static boolean isBoolean(String value) {
        return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
    }
    */
}
