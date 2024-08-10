import org.junit.jupiter.api.Test;
import token.Token;
import token.tokenTypes.TokenTagType;
import token.tokenTypes.TokenValueType;


import java.util.List;

public class SplitTest {

    @Test
    public void test() {
        List<Token> tokens = List.of(
                new Token(TokenValueType.STRING, "let", 1,1 ),
                new Token(TokenValueType.STRING, "let", 1,1 ),
                new Token(TokenTagType.SEMICOLON, ";", 1,1 ),
                new Token(TokenValueType.STRING, "let", 1,1 ),
                new Token(TokenValueType.STRING, "let", 1,1 ),
                new Token(TokenTagType.SEMICOLON, ";", 1,1 )
                //new Token(TokenValueType.STRING, "let", 1,1 )
                );
    }
}
