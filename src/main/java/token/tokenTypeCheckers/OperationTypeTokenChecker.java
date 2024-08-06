package token.tokenTypeCheckers;

import token.tokenTypes.TokenOperandType;
import token.tokenTypes.TokenType;

import java.util.HashMap;
import java.util.Map;

public class OperationTypeTokenChecker implements TypeGetter {
    private static final Map<String, TokenType> operationsMap;

    static {
        operationsMap = new HashMap<>();
        operationsMap.put("+", TokenOperandType.OPERAND);
        operationsMap.put("-", TokenOperandType.OPERAND);
        operationsMap.put("*", TokenOperandType.OPERAND);
        operationsMap.put("/", TokenOperandType.OPERAND);
        operationsMap.put("<", TokenOperandType.OPERAND);
        operationsMap.put(">", TokenOperandType.OPERAND);

    }

    @Override
    public TokenType getType(String word) {
        return operationsMap.getOrDefault(word, null);
    }
}
