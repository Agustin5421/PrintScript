import ast.*;
import ast.literal.StringLiteral;
import lexer.Lexer;
import org.junit.jupiter.api.Test;
import token.Token;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class ParserTest {
    @Test
    public void testVariableDeclaration() {
        Parser parser = new Parser();

        Lexer lexer = ContextProvider.initLexer();
        List<Token> tokens = lexer.extractTokens("let myVar : string = \"Hello\";");
        Program program = parser.parse(tokens);

        assertEquals(1, program.statements().size(), "Program should contain 1 statement");

        VariableDeclaration variableDeclaration = (VariableDeclaration) program.statements().get(0);
        assertEquals("myVar", variableDeclaration.identifier().name(), "Identifier should be 'myVar'");
        assertInstanceOf(StringLiteral.class, variableDeclaration.value(), "Value should be a StringLiteral");
        assertEquals("\"Hello\"", ((StringLiteral) variableDeclaration.value()).value(), "Value should be 'Hello'");
    }

    @Test
    public void testBinaryExpression () {
        Parser parser = ContextProvider.initBinaryExpressionParser();

        Lexer lexer = ContextProvider.initLexer();
        List<Token> tokens = lexer.extractTokens("let myVar : number = 2 + 3 * 2;");
        Program program = parser.parse(tokens);

        assertEquals(1, program.statements().size(), "Program should contain 1 statement");

        VariableDeclaration variableDeclaration = (VariableDeclaration) program.statements().get(0);
        assertEquals("myVar", variableDeclaration.identifier().name(), "Identifier should be 'myVar'");
        assertInstanceOf(BinaryExpression.class, variableDeclaration.value(), "Value should be a BinaryExpression");
    }

    @Test
    public void testCallExpression () {
        Parser parser = new Parser();

        Lexer lexer = ContextProvider.initLexer();
        List<Token> tokens = lexer.extractTokens("println('hello world');");
        Program program = parser.parse(tokens);

        assertEquals(1, program.statements().size(), "Program should contain 1 statement");

        CallExpression callFunc = (CallExpression) program.statements().get(0);
        assertEquals("println", callFunc.methodIdentifier().name(), "Identifier should be 'println'");
        assertInstanceOf(CallExpression.class, callFunc, "Value should be a CallExpression");
    }

    @Test
    public void testAssignmentExpression () {
        Parser parser = new Parser();

        Lexer lexer = ContextProvider.initLexer();
        List<Token> tokens = lexer.extractTokens("myVar = 2 + 3 * 2;");
        Program program = parser.parse(tokens);

        assertEquals(1, program.statements().size(), "Program should contain 1 statement");

        AssignmentExpression variableDeclaration = (AssignmentExpression) program.statements().get(0);
        assertEquals("myVar", variableDeclaration.left().name(), "Identifier should be 'myVar'");
        assertInstanceOf(BinaryExpression.class, variableDeclaration.right(), "Value should be a BinaryExpression");
    }



}
