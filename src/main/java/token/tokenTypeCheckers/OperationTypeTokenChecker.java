package token.tokenTypeCheckers;

import token.tokenTypes.TokenOperandType;
import token.tokenTypes.TokenType;

import java.util.HashMap;
import java.util.Map;

public class OperationTypeTokenChecker implements TypeGetter {
    private static final Map<String, TokenType> operationsMap;

    static {
        operationsMap = new HashMap<>();
        operationsMap.put("+", TokenOperandType.ADD_OPERATION);
        operationsMap.put("-", TokenOperandType.SUB_OPERATION);
        operationsMap.put("*", TokenOperandType.MUL_OPERATION);
        operationsMap.put("/", TokenOperandType.DIV_OPERATION);
        operationsMap.put("<", TokenOperandType.LESS_THAN_OPERATION);
        operationsMap.put(">", TokenOperandType.MORE_THAN_OPERATION);

    }

    @Override
    public TokenType getType(String word) {
        return operationsMap.getOrDefault(word, null);
    }
}
