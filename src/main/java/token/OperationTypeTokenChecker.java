package token;

import java.util.HashMap;
import java.util.Map;

public class OperationTypeTokenChecker implements TypeGetter {
    private static final Map<String, TokenType> operationsMap;

    static {
        operationsMap = new HashMap<>();
        operationsMap.put("+", TokenType.ADD_OPERATION);
        operationsMap.put("-", TokenType.SUB_OPERATION);
        operationsMap.put("*", TokenType.MUL_OPERATION);
        operationsMap.put("/", TokenType.DIV_OPERATION);
    }

    @Override
    public TokenType getType(String word) {
        return operationsMap.getOrDefault(word, null);
    }
}
