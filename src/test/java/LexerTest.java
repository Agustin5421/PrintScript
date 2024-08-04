import org.junit.jupiter.api.Test;
import token.DataTypeTokenChecker;
import token.OperationTypeTokenChecker;
import token.TagTypeTokenChecker;
import token.Token;
import token.TokenType;
import token.TokenTypeGetter;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LexerTest {

    @Test
    public void testLexer() {
        TagTypeTokenChecker TTTChecker = new TagTypeTokenChecker();
        OperationTypeTokenChecker OTTChecker = new OperationTypeTokenChecker();
        DataTypeTokenChecker DTTChecker = new DataTypeTokenChecker();

        TokenTypeGetter TTGetter = new TokenTypeGetter(List.of(TTTChecker, OTTChecker, DTTChecker));

        Lexer lexer = new Lexer(TTGetter);

        assertEquals(13, lexer.extractTokens("2; let code = 9                     5 this is a VARIABLE VAR STRING").size());
        assertEquals(5, lexer.extractTokens("String name = 'Olive;';").size());
        assertEquals(5, lexer.extractTokens("String name = 190;").size());
        assertEquals(9, lexer.extractTokens("String name = 190+2+number;").size());



        List<Token> tokens = lexer.extractTokens("name = 'Olive'");

        assertEquals(TokenType.IDENTIFIER, tokens.get(0).getType());
    }
}
