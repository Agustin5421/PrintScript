package token;

public class DataTypeTokenChecker implements TypeGetter {

    @Override
    public TokenType getType(String value) {
        if (isString(value)) {
            return TokenType.STRING;
        } else if (isInteger(value)) {
            return TokenType.INTEGER;
        } else if (isFloat(value)) {
            return TokenType.FLOAT;
        } else if (isBoolean(value)) {
            return TokenType.BOOLEAN;
        } else {
            return null;
        }
    }

    private static boolean isString(String value) {
        return (value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'"));
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

    private static boolean isBoolean(String value) {
        return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
    }
}
