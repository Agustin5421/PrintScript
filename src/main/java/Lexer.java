import token.Token;
import token.TokenType;
import token.TokenTypeGetter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private final TokenTypeGetter tokenTypeGenerator;

    private static final String TEXT_PATTERNS =
            "\"[^\"]*\"" +                                     // Text between double quotes
                    "|'[^']*'" +                               // Text between single quotes
                    "|\\d+" +                                  // Integers
                    "|[a-zA-Z_][a-zA-Z_0-9]*" +                // Identifiers and keywords
                    "|[=;]" +                                  // Equal
                    "|[+\\-*/]";                               // Arithmetic Operands



    public Lexer(TokenTypeGetter tokenTypeGenerator) {
        this.tokenTypeGenerator = tokenTypeGenerator;
    }



    public List<Token> extractTokens(String code) {
        List<Token> tokens = new ArrayList<>();
        Pattern pattern = Pattern.compile(TEXT_PATTERNS);
        Matcher matcher = pattern.matcher(code);

        Position position = new Position(1, 0);
        int currentIndex = 0;

        while (matcher.find()) {
            String word = matcher.group();
            int start = matcher.start();

            UpdateRowCol(code, currentIndex, start, position);
            currentIndex = matcher.end();

            //Create Token
            TokenType type = tokenTypeGenerator.getType(word);
            Token token = new Token(type, word, position.row, position.col);
            tokens.add(token);

            //Update col
            position.col += word.length();
        }

        return tokens;
    }

    private static void UpdateRowCol(String code, int currentIndex, int start, Position position) {
        // Update row and col
        for (int i = currentIndex; i < start; i++) {
            if (code.charAt(i) == '\n') {
                position.row++;
                position.col = 0;
            } else {
                position.col++;
            }
        }
    }

    private static class Position {
        int row;
        int col;

        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
}