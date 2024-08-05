import lexer.Lexer;
import org.junit.jupiter.api.Test;
import token.tokenTypeCheckers.DataTypeTokenChecker;
import token.tokenTypeCheckers.OperationTypeTokenChecker;
import token.tokenTypeCheckers.TagTypeTokenChecker;
import token.Token;
import token.tokenTypes.TokenTagType;
import token.tokenTypeCheckers.TokenTypeChecker;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LexerTest {

    @Test
    public void testLexer() {
        Lexer lexer = initLexer();

        assertEquals(13, lexer.extractTokens("2; let code = 9                     5 this is a VARIABLE VAR STRING").size());
        assertEquals(5, lexer.extractTokens("String name = Olive;").size());
        assertEquals(5, lexer.extractTokens("String name = 190;").size());
        assertEquals(9, lexer.extractTokens("String name = 190+2+number;").size());

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
                    "name = 2",
                    "\"Hello World\" + 3.14",
                    "if (x > 0) { x = x + 1; }", //If not yet implemented
                    "\n abc",
                    "'string' true 45",
                    "a = 'test' * 2.5"
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

    private static Lexer initLexer() {
        TagTypeTokenChecker tagTypeChecker = new TagTypeTokenChecker();
        OperationTypeTokenChecker operationTypeChecker = new OperationTypeTokenChecker();
        DataTypeTokenChecker dataTypeChecker = new DataTypeTokenChecker();

        TokenTypeChecker tokenTypeChecker = new TokenTypeChecker(List.of(tagTypeChecker, operationTypeChecker, dataTypeChecker));

        return new Lexer(tokenTypeChecker);
    }
}
