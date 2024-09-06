package splitters;

import java.util.List;
import token.Token;

public record SplitResult(List<Token> statement, List<Token> remainingTokens) {}
