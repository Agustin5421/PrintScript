package token;

public class DataTypeTokenVal implements TypeGetter {
    @Override
    public boolean checkType(String token) {
        return false;
    }

    @Override
    public TokenType getType() {
        return null;
    }
}
