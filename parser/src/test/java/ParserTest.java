import ast.expressions.BinaryExpression;
import ast.literal.StringLiteral;
import ast.root.ASTNode;
import ast.root.Program;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.VariableDeclaration;
import lexer.Lexer;
import org.junit.jupiter.api.Test;
import parsers.Parser;
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
    public void expressionWithVariablesTest() {
        Parser parser = ContextProvider.initBinaryExpressionParser();

        Lexer lexer = ContextProvider.initLexer();
        List<Token> tokens = lexer.extractTokens("let myVar : number = 2 + 3 * A;");
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

    @Test
    public void completeTest () {
        Parser parser = new Parser();

        Lexer lexer = ContextProvider.initLexer();
        List<Token> tokens = lexer.extractTokens(
                "let myVar : number = 2 + 3 * 2;" +
                "println(myVar);" +
                "myVar = 'Hello World';" +
                "println(myVar);");

        Program program = parser.parse(tokens);

        assertEquals(4, program.statements().size(), "Program should contain 1 statement");

        ASTNode first = program.statements().get(0);
        ASTNode second = program.statements().get(1);
        ASTNode third = program.statements().get(2);
        ASTNode fourth = program.statements().get(3);

        assertEquals("myVar", ((VariableDeclaration) first).identifier().name(), "Identifier should be 'myVar'");
        assertInstanceOf(BinaryExpression.class, ((VariableDeclaration) first).value(), "Value should be a BinaryExpression");

        assertEquals("println", ((CallExpression) second).methodIdentifier().name(), "Identifier should be 'println'");
        assertInstanceOf(CallExpression.class, second, "Value should be a CallExpression");

        assertEquals("myVar", ((AssignmentExpression) third).left().name(), "Identifier should be 'myVar'");
        assertInstanceOf(StringLiteral.class, ((AssignmentExpression) third).right(), "Value should be a StringLiteral");

        assertEquals("println", ((CallExpression) fourth).methodIdentifier().name(), "Identifier should be 'println'");
        assertInstanceOf(CallExpression.class, fourth, "Value should be a CallExpression");


    }
}
