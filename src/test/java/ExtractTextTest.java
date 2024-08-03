import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

public class ExtractTextTest {

    @Test
    public void testTokenize() {
        Lexer lexer = new Lexer(new TokenTypeGetter(List.of(new DataTypeTokenVal())));

        // Caso 1: Código de ejemplo
        String code1 = "public final String name = \"Olive\"; 'hello' 123";
        List<String> tokens1 = lexer.extractWords(code1);
        assertEquals(9, tokens1.size());
        assertEquals("public", tokens1.get(0));
        assertEquals("final", tokens1.get(1));
        assertEquals("String", tokens1.get(2));
        assertEquals("name", tokens1.get(3));
        assertEquals("=", tokens1.get(4));
        assertEquals("\"Olive\"", tokens1.get(5));
        assertEquals(";", tokens1.get(6));
        assertEquals("'hello'", tokens1.get(7));
        assertEquals("123", tokens1.get(8));

        // Caso 2: Código con solo identificadores y enteros
        String code2 = "int a = 10; char b = 'b';";
        List<String> tokens2 = lexer.extractWords(code2);
        assertEquals(10, tokens2.size());
        assertEquals("int", tokens2.get(0));
        assertEquals("a", tokens2.get(1));
        assertEquals("=", tokens2.get(2));
        assertEquals("10", tokens2.get(3));
        assertEquals(";", tokens2.get(4));
        assertEquals("char", tokens2.get(5));
        assertEquals("b", tokens2.get(6));
        assertEquals("=", tokens2.get(7));
        assertEquals("'b'", tokens2.get(8));
        assertEquals(";", tokens2.get(9));

        // Caso 3: Código con espacios y tabulaciones
        String code3 = "String str = \"test\"; \n int num = 42;";
        List<String> tokens3 = lexer.extractWords(code3);
        assertEquals(10, tokens3.size());
        assertEquals("String", tokens3.get(0));
        assertEquals("str", tokens3.get(1));
        assertEquals("=", tokens3.get(2));
        assertEquals("\"test\"", tokens3.get(3));
        assertEquals(";", tokens3.get(4));
        assertEquals("int", tokens3.get(5));
        assertEquals("num", tokens3.get(6));
        assertEquals("=", tokens3.get(7));
        assertEquals("42", tokens3.get(8));
        assertEquals(";", tokens3.get(9));

        // Caso 4: Código con comillas simples
        String code4 = "char c = 'c';";
        List<String> tokens4 = lexer.extractWords(code4);
        assertEquals(5, tokens4.size());
        assertEquals("char", tokens4.get(0));
        assertEquals("c", tokens4.get(1));
        assertEquals("=", tokens4.get(2));
        assertEquals("'c'", tokens4.get(3));
        assertEquals(";", tokens4.get(4));
    }
}
