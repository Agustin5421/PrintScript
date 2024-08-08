package lexer;

import org.junit.jupiter.api.Test;
import token.tokenTypeCheckers.*;
import token.Token;
import token.tokenTypes.TokenTagType;
import token.tokenTypes.TokenValueType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LexerTest {

    @Test
    public void testLexer() {
        Lexer lexer = initLexer();

        assertEquals(13, lexer.extractTokens("2; let code = 9                     5 this is a VARIABLE VAR STRING").size());
        assertEquals(5, lexer.extractTokens("string name = Olive;").size());
        assertEquals(5, lexer.extractTokens("string name = 190;").size());
        assertEquals(9, lexer.extractTokens("string name = 190+2+number;").size());

        List<Token> tokens = lexer.extractTokens("name = 'hello world + 2' + 'hola'" +
                "\n abc");

        assertEquals(TokenTagType.IDENTIFIER, tokens.get(0).getType());
    }

    @Test
    public void testLexer1(){

        // Initialize the token type checkers
        Lexer lexer = initLexer();

        // Define the test cases
            String[] testCases = {
                    "2a = 'test' * 2.5",
                    "name = 2",
                    "\"Hello World\" + 3.14",
                    "\n abc",
                    "'string' true 45"
            };

            // Run the test cases
            for (String testCase : testCases) {
                System.out.println("--------------------------------------------------------------------");
                List<Token> tokens = lexer.extractTokens(testCase);
                System.out.println("Input: " + testCase);
                tokens.forEach(token -> System.out.println("Token: " + token));
        }
        System.out.println("--------------------------------------------------------------------");
    }

    @Test
    public void testSeveralLinesCode() {
        Lexer lexer = initLexer();

        String code = "This\nis\nan\nexample\n'hi\nhey'";

        List<Token> tokens = lexer.extractTokens(code);

        List<Token> tokensToCompare = List.of(
                new Token(TokenTagType.IDENTIFIER, "This", 1, 0),
                new Token(TokenTagType.IDENTIFIER, "is", 2, 0),
                new Token(TokenTagType.IDENTIFIER, "an", 3, 0),
                new Token(TokenTagType.IDENTIFIER, "example", 4, 0),
                new Token(TokenValueType.STRING, "'hi\nhey'", 5, 0)
                );

        for (int i = 0; i < tokens.size(); i++) {
            Token tokenToComp = tokensToCompare.get(i);
            Token token = tokens.get(i);
            assertEquals(tokenToComp.getValue(), token.getValue());
            assertEquals(tokenToComp.getType(), token.getType());
            assertEquals(tokenToComp.getRow(), token.getRow());
            assertEquals(tokenToComp.getCol(), token.getCol());
        }
    }

    private static Lexer initLexer() {
        TagTypeTokenChecker tagTypeChecker = new TagTypeTokenChecker();
        OperationTypeTokenChecker operationTypeChecker = new OperationTypeTokenChecker();
        DataTypeTokenChecker dataTypeChecker = new DataTypeTokenChecker();
        IdentifierTypeChecker identifierTypeChecker = new IdentifierTypeChecker();

        TokenTypeChecker tokenTypeChecker = new TokenTypeChecker(List.of(tagTypeChecker, operationTypeChecker, dataTypeChecker, identifierTypeChecker));

        return new Lexer(tokenTypeChecker);
    }
}
