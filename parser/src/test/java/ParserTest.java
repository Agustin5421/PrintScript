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
import parsers.Parser;
import token.Token;

public class ParserTest {
  private final Parser parser;
  private final Lexer lexer;

  public ParserTest(Parser parser, Lexer lexer) {
    this.parser = parser;
    this.lexer = lexer;
  }

  public void runAllTests(String version) {
    switch (version) {
      case "1.0" -> testV1();
      case "1.1" -> testV2();
      default -> throw new IllegalArgumentException("Invalid version");
    }
  }

  public void testV1() {
    testVariableDeclaration();
    testBinaryExpression();
    expressionWithVariablesTest();
    testCallExpression();
    testAssignmentExpression();
    completeTest();
    unsupportedStatementExceptionTest();
    unsupportedExpressionExceptionTest();
    syntaxExceptionTest();
    unsupportedDataTypeExceptionTest();
  }

  public void testV2() {
    testV1();
  }

  public void testVariableDeclaration() {
    List<Token> tokens = lexer.tokenize("let myVar : string = 2;");
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

  public void testBinaryExpression() {
    List<Token> tokens = lexer.tokenize("let myVar : number = (2+2) * (2/2) + 2;");
    Program program = parser.parse(tokens);

    assertEquals(1, program.statements().size(), "Program should contain 1 statement");

    VariableDeclaration variableDeclaration = (VariableDeclaration) program.statements().get(0);
    assertEquals("myVar", variableDeclaration.identifier().name(), "Identifier should be 'myVar'");
    assertEquals(
        AstNodeType.BINARY_EXPRESSION,
        variableDeclaration.expression().getType(),
        "Value should be a BinaryExpression");
  }

  public void expressionWithVariablesTest() {
    List<Token> tokens = lexer.tokenize("let myVar : number = 2 + 3 * A;");
    Program program = parser.parse(tokens);

    assertEquals(1, program.statements().size(), "Program should contain 1 statement");

    VariableDeclaration variableDeclaration = (VariableDeclaration) program.statements().get(0);
    assertEquals("myVar", variableDeclaration.identifier().name(), "Identifier should be 'myVar'");
    assertEquals(
        AstNodeType.BINARY_EXPRESSION,
        variableDeclaration.expression().getType(),
        "Type should be a BinaryExpression");
  }

  public void testCallExpression() {
    List<Token> tokens = lexer.tokenize("println('hello world');");
    Program program = parser.parse(tokens);

    assertEquals(1, program.statements().size(), "Program should contain 1 statement");

    CallExpression callFunc = (CallExpression) program.statements().get(0);
    assertEquals("println", callFunc.methodIdentifier().name(), "Identifier should be 'println'");
    assertEquals(
        AstNodeType.CALL_EXPRESSION, callFunc.getType(), "Type should be a CallExpression");
  }

  public void testAssignmentExpression() {
    List<Token> tokens = lexer.tokenize("myVar = 2 + 3 * 2;");
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

  public void completeTest() {
    List<Token> tokens =
        lexer.tokenize(
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

  public void unsupportedStatementExceptionTest() {
    List<Token> tokens = lexer.tokenize("null;");
    assertThrows(UnsupportedStatementException.class, () -> parser.parse(tokens));
  }

  public void unsupportedExpressionExceptionTest() {
    List<Token> tokens = lexer.tokenize("myVar = hello he * 2;");
    assertThrows(UnsupportedExpressionException.class, () -> parser.parse(tokens));
  }

  public void syntaxExceptionTest() {
    List<Token> tokens = lexer.tokenize("let myVar : number = 2 + 3 * 2");
    assertThrows(SyntaxException.class, () -> parser.parse(tokens));
  }

  public void unsupportedDataTypeExceptionTest() {
    List<Token> tokens = lexer.tokenize("let myVar : boolean = 2 + 3 * 2;");
    assertThrows(UnsupportedDataType.class, () -> parser.parse(tokens));
  }

  /*
  @Test
  public void testIfStatement() {
    List<Token> tokens =
        lexer.tokenize("if (true) { println('hello world'); } else { println('goodbye world'); }");

    assertThrows(UnsupportedStatementException.class, () -> parser.parse(tokens));
  }
   */

}
