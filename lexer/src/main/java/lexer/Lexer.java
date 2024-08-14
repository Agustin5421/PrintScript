package lexer;

import token.Token;
import token.Position;
import token.tokenTypes.TokenType;
import token.tokenTypeCheckers.TokenTypeChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private final TokenTypeChecker tokenTypeGetter;

    private static final String TEXT_PATTERNS =
            "\"[^\"]*\"" +                                     // Text between double quotes
                    "|'[^']*'" +                               // Text between single quotes
                    "|\\d+(\\.\\d+)?[a-zA-Z_]*" +              // Numbers with optional decimal part and units (with letters)
                    "|\\b[a-zA-Z_][a-zA-Z\\d_]*\\b" +          // Identifiers and keywords
                    "|[=;:]" +                                 // Equal, semicolon, and colon
                    "|[+\\-*/%]" +                             // Arithmetic operands
                    "|[()<>{},]" +                             // Parentheses, angle brackets, curly braces, comma
                    "|[.]" +                                   // Period (for decimal for decimal points or standalone)
                    "|\\S";                                    // Any other single character (mismatch), excluding spaces





    public Lexer(TokenTypeChecker tokenTypeGetter) {
        this.tokenTypeGetter = tokenTypeGetter;
    }



    public List<Token> extractTokens(String code) {
        List<Token> tokens = new ArrayList<>();
        Pattern pattern = Pattern.compile(TEXT_PATTERNS);
        Matcher matcher = pattern.matcher(code);

//        Position position = new Position(1, 1);
        Position initialPosition = new Position(1, 1);
        int currentIndex = 0;

        while (matcher.find()) {
            String word = matcher.group();
            int start = matcher.start();
            int end = matcher.end();

            initialPosition = updatePosition(code, currentIndex, start, initialPosition);
            Position finalPosition = updatePosition(code, start, end, initialPosition);

            currentIndex = end;

            //Create Token
            TokenType type = tokenTypeGetter.getType(word);
            Token token = new Token(type, word, initialPosition, finalPosition);
            tokens.add(token);

            //Update position
            initialPosition = finalPosition;
        }

        return tokens;
    }

    private Position updatePosition(String code, int initialIndex, int finalIndex, Position position) {
        int row = position.row();
        int col = position.col();

        for (int i = initialIndex; i < finalIndex; i++) {
            if (code.charAt(i) == '\n') {
                row++;
                col = 1;
            } else {
                col++;
            }
        }

        return new Position(row, col);
    }
}
