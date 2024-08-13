import ast.literal.NumberLiteral;
import ast.Program;
import ast.literal.StringLiteral;
import ast.VariableDeclaration;
import lexer.Lexer;
import org.junit.jupiter.api.Test;
import parsers.StatementParser;
import parsers.VariableDeclarationParser;
import token.Token;
import token.tokenTypeCheckers.*;
import token.tokenTypes.TokenDataType;
import token.tokenTypes.TokenTagType;
import token.tokenTypes.TokenValueType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class StatementParserTest {

    @Test
    public void test() {
        Program program2 = getProgram();

        // Verifies the program contains 2 statements
        assertEquals(2, program2.getStatements().size(), "Program should contain 2 statements");

        // Verifies the first declaration
        VariableDeclaration firstDeclaration = (VariableDeclaration) program2.getStatements().get(0);
        assertEquals("myVar", firstDeclaration.identifier().getName(), "First identifier should be 'myVar'");
        assertInstanceOf(StringLiteral.class, firstDeclaration.literal(), "First literal should be a LiteralString");
        assertEquals("Hello", (firstDeclaration.literal()).getValue(), "First literal value should be 'Hello'");

        // Verifies the second declaration
        VariableDeclaration secondDeclaration = (VariableDeclaration) program2.getStatements().get(1);
        assertEquals("myNumber", secondDeclaration.identifier().getName(), "Second identifier should be 'myNumber'");
        assertInstanceOf(NumberLiteral.class, secondDeclaration.literal(), "Second literal should be a LiteralNumber");
        assertEquals(42, (secondDeclaration.literal()).getValue(), "Second literal value should be 42");

    }

    private static Program getProgram() {
        VariableDeclarationParser variableDeclarationParser = new VariableDeclarationParser();
        StatementParser statementParser = new StatementParser(List.of(variableDeclarationParser));

        Parser parser = new Parser(List.of(statementParser));


        List<Token> tokens2 = List.of(
                new Token(TokenTagType.DECLARATION, "let", 0, 0),
                new Token(TokenTagType.IDENTIFIER, "myVar", 0, 4),
                new Token(TokenTagType.SYNTAX, ":", 0, 9),
                new Token(TokenDataType.STRING_TYPE, "string", 0, 11),
                new Token(TokenTagType.ASSIGNATION, "=", 0, 18),
                new Token(TokenValueType.STRING, "Hello", 0, 20),
                new Token(TokenTagType.SEMICOLON, ";", 0, 27),

                new Token(TokenTagType.DECLARATION, "let", 1, 0),
                new Token(TokenTagType.IDENTIFIER, "myNumber", 1, 4),
                new Token(TokenTagType.SYNTAX, ":", 1, 12),
                new Token(TokenDataType.STRING_TYPE, "number", 1, 14),
                new Token(TokenTagType.ASSIGNATION, "=", 1, 21),
                new Token(TokenValueType.NUMBER, "42", 1, 23),
                new Token(TokenTagType.SEMICOLON, ";", 1, 25))
                ;

        return parser.parse(tokens2);
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
