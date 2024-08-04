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
            "\"[^\"]*\"" +                              // Cadenas entre comillas dobles
                    "|'[^']*'" +                        // Cadenas entre comillas simples
                    "|\\d+" +                           // Enteros
                    "|[a-zA-Z_][a-zA-Z_0-9]*" +         // Identificadores y palabras clave
                    "|[=;]" +                           // Operadores y signos de puntuación
                    "|[+\\-*/]";                        // Operadores aritméticos

    public Lexer(TokenTypeGetter tokenTypeGenerator) {
        this.tokenTypeGenerator = tokenTypeGenerator;
    }


    public List<String> extractWords(String code) {
        List<String> codeAsList = new ArrayList<>();
        Pattern pattern = Pattern.compile(TEXT_PATTERNS);
        Matcher matcher = pattern.matcher(code);
        while (matcher.find()) {
            codeAsList.add(matcher.group());
        }
        return codeAsList;
    }


    //TODO: no idea how to check col/row
    public List<Token> extractTokens(String code) {
        int row = 0;
        int col = 0;
        List<Token> tokens = new ArrayList<>();
        List<String> words = extractWords(code);
        for (String word : words) {
            TokenType type = tokenTypeGenerator.getType(word);
            Token token = new Token(type, word, row, col);
            tokens.add(token);
        }
        return tokens;
    }
}
