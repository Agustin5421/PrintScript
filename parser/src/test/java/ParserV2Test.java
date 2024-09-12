import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import ast.root.AstNode;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.IfStatement;
import ast.statements.VariableDeclaration;
import factory.ParserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parsers.Parser;

public class ParserV2Test extends CommonParserTests {
  private Parser parserV2;

  @BeforeEach
  public void setUp() {
    parserV2 = ParserFactory.getParser("1.1");
  }

  @Override
  protected Parser getParser() {
    return parserV2;
  }

  @Test
  public void testIfStatement() {
    Parser parser =
        setParser(
            "if (true) { if(a){hola=2;} let name: string = \"Oliver\";} else {a=3; a=5; a=6;}",
            getParser());
    AstNode node = parser.next();
    assertInstanceOf(IfStatement.class, node);
  }

  // TODO: solve these tests
  /*
  @Test
  public void testNoElse() {
    Parser parser = setParser("if (true) { let name: string = \"Oliver\";}", getParser());
    AstNode node = parser.next();
    assertInstanceOf(IfStatement.class, node);
  }

   */

  @Test
  public void testConstDeclaration() {
    Parser parser = setParser("const myBool: boolean = \"true\";", getParser());
    AstNode node = parser.next();
    assertInstanceOf(VariableDeclaration.class, node);
  }

  @Test
  public void testReadInputFunction() {
    Parser parser = setParser("readInput(this, is, a, test);", getParser());
    CallExpression node = (CallExpression) parser.next();
    assertInstanceOf(CallExpression.class, node);
    assertEquals("readInput", node.methodIdentifier().name());
    assertEquals(4, node.arguments().size());
  }

  @Test
  public void testReadEnvFunction() {
    Parser parser = setParser("readEnv('this is a string', is, also, a, test);", getParser());
    CallExpression node = (CallExpression) parser.next();
    assertInstanceOf(CallExpression.class, node);
    assertEquals("readEnv", node.methodIdentifier().name());
    assertEquals(5, node.arguments().size());
  }

  @Test
  public void testReadEnvAsFunctionExpression() {
    Parser parser = setParser("a = readEnv('this is a string', is, also, a, test);", getParser());
    AssignmentExpression node = (AssignmentExpression) parser.next();
    assertInstanceOf(AssignmentExpression.class, node);
    CallExpression callExpression = (CallExpression) node.right();
    assertEquals("readEnv", callExpression.methodIdentifier().name());
    assertEquals(5, callExpression.arguments().size());
    assertEquals("a", node.left().name());
  }

  @Test
  public void testIfWithoutElse() {
    Parser parser = setParser("if (true) {x = 'omg it worked';}", getParser());
    IfStatement node = (IfStatement) parser.next();
    assertEquals(1, node.getThenBlockStatement().size());
  }
}
