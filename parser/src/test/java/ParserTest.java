import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ast.literal.NumberLiteral;
import ast.root.AstNode;
import ast.root.AstNodeType;
import ast.root.Program;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.VariableDeclaration;
import exceptions.SyntaxException;
import exceptions.UnsupportedDataType;
import exceptions.UnsupportedExpressionException;
import exceptions.UnsupportedStatementException;
import java.util.List;
import lexer.Lexer;
import org.junit.jupiter.api.Test;
import parsers.Parser;
import token.Token;

public class ParserTest {
  @Test
  public void testVariableDeclaration() {
    Parser parser = new Parser();

    Lexer lexer = ContextProvider.initLexer();
    List<Token> tokens = lexer.extractTokens("let myVar : string = 2;");
    Program program = parser.parse(tokens);

    assertEquals(1, program.statements().size(), "Program should contain 1 statement");

    VariableDeclaration variableDeclaration = (VariableDeclaration) program.statements().get(0);
    assertEquals("myVar", variableDeclaration.identifier().name(), "Identifier should be 'myVar'");
    assertEquals(
        AstNodeType.VARIABLE_DECLARATION,
        variableDeclaration.getType(),
        "Type should be VariableDeclaration");
    assertInstanceOf(
        NumberLiteral.class, variableDeclaration.expression(), "Value should be a StringLiteral");
    assertEquals(
        AstNodeType.NUMBER_LITERAL,
        (variableDeclaration.expression().getType()),
        "Expression should be NumberLiteral");
  }

  @Test
  public void testBinaryExpression() {
    Parser parser = ContextProvider.initBinaryExpressionParser();

    Lexer lexer = ContextProvider.initLexer();
    List<Token> tokens = lexer.extractTokens("let myVar : number = (2+2) * (2/2) + 2;");
    Program program = parser.parse(tokens);

    assertEquals(1, program.statements().size(), "Program should contain 1 statement");

    VariableDeclaration variableDeclaration = (VariableDeclaration) program.statements().get(0);
    assertEquals("myVar", variableDeclaration.identifier().name(), "Identifier should be 'myVar'");
    assertEquals(
        AstNodeType.BINARY_EXPRESSION,
        variableDeclaration.expression().getType(),
        "Value should be a BinaryExpression");
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
    assertEquals(
        AstNodeType.BINARY_EXPRESSION,
        variableDeclaration.expression().getType(),
        "Type should be a BinaryExpression");
  }

  @Test
  public void testCallExpression() {
    Parser parser = new Parser();

    Lexer lexer = ContextProvider.initLexer();
    List<Token> tokens = lexer.extractTokens("println('hello world');");
    Program program = parser.parse(tokens);

    assertEquals(1, program.statements().size(), "Program should contain 1 statement");

    CallExpression callFunc = (CallExpression) program.statements().get(0);
    assertEquals("println", callFunc.methodIdentifier().name(), "Identifier should be 'println'");
    assertEquals(
        AstNodeType.CALL_EXPRESSION, callFunc.getType(), "Type should be a CallExpression");
  }

  @Test
  public void testAssignmentExpression() {
    Parser parser = new Parser();

    Lexer lexer = ContextProvider.initLexer();
    List<Token> tokens = lexer.extractTokens("myVar = 2 + 3 * 2;");
    Program program = parser.parse(tokens);

    assertEquals(1, program.statements().size(), "Program should contain 1 statement");

    AssignmentExpression variableDeclaration = (AssignmentExpression) program.statements().get(0);
    assertEquals("myVar", variableDeclaration.left().name(), "Identifier should be 'myVar'");
    assertEquals(
        AstNodeType.ASSIGNMENT_EXPRESSION,
        variableDeclaration.getType(),
        "Type should be a Assignment Expression");
    assertEquals(
        AstNodeType.IDENTIFIER,
        variableDeclaration.left().getType(),
        "Type should be a Identifier");
    assertEquals(
        AstNodeType.BINARY_EXPRESSION,
        variableDeclaration.right().getType(),
        "Type should be a BinaryExpression");
  }

  @Test
  public void completeTest() {
    Parser parser = new Parser();

    Lexer lexer = ContextProvider.initLexer();
    List<Token> tokens =
        lexer.extractTokens(
            "let myVar : number = 2 + 3 * 2;"
                + "println(myVar);"
                + "myVar = 'Hello World';"
                + "println(myVar);");

    Program program = parser.parse(tokens);

    assertEquals(4, program.statements().size(), "Program should contain 4 statement");

    AstNode first = program.statements().get(0);

    assertEquals(
        "myVar", ((VariableDeclaration) first).identifier().name(), "Identifier should be 'myVar'");
    assertEquals(
        AstNodeType.BINARY_EXPRESSION,
        ((VariableDeclaration) first).expression().getType(),
        "Value should be a BinaryExpression");

    AstNode second = program.statements().get(1);

    assertEquals(
        "println",
        ((CallExpression) second).methodIdentifier().name(),
        "Identifier should be 'println'");
    assertEquals(AstNodeType.CALL_EXPRESSION, second.getType(), "Value should be a CallExpression");

    AstNode third = program.statements().get(2);

    assertEquals(
        "myVar", ((AssignmentExpression) third).left().name(), "Identifier should be 'myVar'");
    assertEquals(
        AstNodeType.ASSIGNMENT_EXPRESSION, (third.getType()), "Value should be a StringLiteral");

    assertEquals(
        AstNodeType.STRING_LITERAL,
        ((AssignmentExpression) third).right().getType(),
        "Value should be a StringLiteral");

    AstNode fourth = program.statements().get(3);

    assertEquals(
        "println",
        ((CallExpression) fourth).methodIdentifier().name(),
        "Identifier should be 'println'");
    assertInstanceOf(CallExpression.class, fourth, "Value should be a CallExpression");
  }

  @Test
  public void unsupportedStatementExceptionTest() {
    Parser parser = new Parser();

    Lexer lexer = ContextProvider.initLexer();
    List<Token> tokens = lexer.extractTokens("null;");
    assertThrows(UnsupportedStatementException.class, () -> parser.parse(tokens));
  }

  @Test
  public void unsupportedExpressionExceptionTest() {
    Parser parser = new Parser();

    Lexer lexer = ContextProvider.initLexer();
    List<Token> tokens = lexer.extractTokens("myVar = hello he * 2;");
    assertThrows(UnsupportedExpressionException.class, () -> parser.parse(tokens));
  }

  @Test
  public void syntaxExceptionTest() {
    // syntax exception
    Parser parser = new Parser();

    Lexer lexer = ContextProvider.initLexer();
    List<Token> tokens = lexer.extractTokens("let myVar : number = 2 + 3 * 2");
    assertThrows(SyntaxException.class, () -> parser.parse(tokens));
  }

  @Test
  public void unsupportedDataTypeExceptionTest() {
    Parser parser = new Parser();

    Lexer lexer = ContextProvider.initLexer();
    List<Token> tokens = lexer.extractTokens("let myVar : boolean = 2 + 3 * 2;");
    assertThrows(UnsupportedDataType.class, () -> parser.parse(tokens));
  }
}
