import ast.NumberLiteral;
import ast.Program;
import ast.StringLiteral;
import ast.VariableDeclaration;
import lexer.Lexer;
import org.junit.jupiter.api.Test;
import parsers.AssignmentExpressionParser;
import parsers.CallExpressionParser;
import parsers.StatementParser;
import parsers.VariableDeclarationParser;
import token.Token;
import token.Position;
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
        assertEquals(3, program2.getStatements().size(), "Program should contain 2 statements");

        // Verifies the first declaration
        VariableDeclaration firstDeclaration = (VariableDeclaration) program2.getStatements().get(0);
        assertEquals("myVar", firstDeclaration.getIdentifier().getName(), "First identifier should be 'myVar'");
        assertInstanceOf(StringLiteral.class, firstDeclaration.getLiteral(), "First literal should be a LiteralString");
        assertEquals("Hello", (firstDeclaration.getLiteral()).getValue(), "First literal value should be 'Hello'");

        // Verifies the second declaration
        VariableDeclaration secondDeclaration = (VariableDeclaration) program2.getStatements().get(1);
        assertEquals("myNumber", secondDeclaration.getIdentifier().getName(), "Second identifier should be 'myNumber'");
        assertInstanceOf(NumberLiteral.class, secondDeclaration.getLiteral(), "Second literal should be a LiteralNumber");
        assertEquals(42, (secondDeclaration.getLiteral()).getValue(), "Second literal value should be 42");

    }


    @Test
    public void test1() {
        Parser parser = getParser();


        Lexer lexer = initLexer();
        List<Token>  tokens = lexer.extractTokens("println ('hola');");
        Program program = parser.parse(tokens);

        System.out.println("done");

    }

    private static Program getProgram() {
        Parser parser = getParser();

        Position position = new Position(0, 0);

        List<Token> tokens2 = List.of(
                new Token(TokenTagType.DECLARATION, "let", position, position),
                new Token(TokenTagType.IDENTIFIER, "myVar", position, position),
                new Token(TokenTagType.SYNTAX, ":", position, position),
                new Token(TokenDataType.STRING_TYPE, "string", position, position),
                new Token(TokenTagType.ASSIGNATION, "=", position, position),
                new Token(TokenValueType.STRING, "Hello", position, position),
                new Token(TokenTagType.SEMICOLON, ";", position, position),

                new Token(TokenTagType.DECLARATION, "let", position, position),
                new Token(TokenTagType.IDENTIFIER, "myNumber", position, position),
                new Token(TokenTagType.SYNTAX, ":", position, position),
                new Token(TokenDataType.STRING_TYPE, "number", position, position),
                new Token(TokenTagType.ASSIGNATION, "=", position, position),
                new Token(TokenValueType.NUMBER, "42", position, position),
                new Token(TokenTagType.SEMICOLON, ";", position, position),

                new Token(TokenTagType.IDENTIFIER, "name", position, position),
                new Token(TokenTagType.ASSIGNATION, "=", position, position),
                new Token(TokenValueType.STRING, "agustin", position, position),
                new Token(TokenTagType.SEMICOLON, ";", position, position)
                )
                ;

        return parser.parse(tokens2);
    }

    private static Parser getParser() {
        VariableDeclarationParser variableDeclarationParser = new VariableDeclarationParser();
        AssignmentExpressionParser assignmentExpressionParser = new AssignmentExpressionParser();
        CallExpressionParser callExpressionParser = new CallExpressionParser();
        StatementParser statementParser = new StatementParser(List.of(variableDeclarationParser, assignmentExpressionParser, callExpressionParser));

        return new Parser(List.of(statementParser));
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
