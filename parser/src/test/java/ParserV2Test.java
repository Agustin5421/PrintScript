import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import ast.root.AstNode;
import ast.statements.CallExpression;
import ast.statements.IfStatement;
import ast.statements.VariableDeclaration;
import exceptions.InvalidConstReassignmentException;
import exceptions.MismatchTypeException;
import exceptions.VariableNotDeclaredException;
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
  public void testConstDeclaration() {
    Parser parser = setParser("const myBool: boolean = true;", getParser());
    AstNode node = parser.next();
    assertInstanceOf(VariableDeclaration.class, node);
  }

  @Test
  public void testReadInputFunction() {
    Parser parser = setParser("readInput('this', 'is', 'a', 'test');", getParser());
    CallExpression node = (CallExpression) parser.next();
    assertInstanceOf(CallExpression.class, node);
    assertEquals("readInput", node.methodIdentifier().name());
    assertEquals(4, node.arguments().size());
  }

  @Test
  public void testReadInputWithExpression() {
    Parser parser =
        setParser("let input: string = readInput(\"Enter\" + \"something\");", getParser());
    VariableDeclaration node = (VariableDeclaration) parser.next();
    assertInstanceOf(VariableDeclaration.class, node);
    assertEquals("input", node.identifier().name());
    CallExpression callExpression = (CallExpression) node.expression();
    assertEquals("readInput", callExpression.methodIdentifier().name());
  }

  @Test
  public void testReadEnvFunction() {
    Parser parser = setParser("readEnv('this is a string');", getParser());
    CallExpression node = (CallExpression) parser.next();
    assertInstanceOf(CallExpression.class, node);
    assertEquals("readEnv", node.methodIdentifier().name());
    assertEquals(1, node.arguments().size());
  }

  @Test
  public void testReadEnvAsFunctionExpression() {
    Parser parser =
        setParser("let a : string = readEnv('this is a string', 2, 3, 4, 5);", getParser());
    VariableDeclaration node = (VariableDeclaration) parser.next();
    assertInstanceOf(VariableDeclaration.class, node);
    CallExpression callExpression = (CallExpression) node.expression();
    assertEquals("readEnv", callExpression.methodIdentifier().name());
    assertEquals(5, callExpression.arguments().size());
    assertEquals("a", node.identifier().name());
  }

  @Test
  public void testInvalidTypeDeclaration() {
    Parser parser = setParser("let myVar : number = 'hola' + 2 ;", getParser());
    assertThrows(MismatchTypeException.class, parser::next);
  }

  @Test
  public void testIfWithoutElse() {
    Parser parser = setParser("if (true) {let x : string = 'omg it worked';}", getParser());
    IfStatement node = (IfStatement) parser.next();
    assertEquals(1, node.getThenBlockStatement().size());
  }

  @Test
  public void test() {
    Parser parser = setParser("let a : boolean = false; if (a) {} else {c=2;}", getParser());
    parser.next();
    assertThrows(VariableNotDeclaredException.class, parser::next);
  }

  @Test
  public void testConst() {
    Parser parser =
        setParser(
            """
                        const booleanValue : boolean = true;
                        if(booleanValue) {
                            println("if statement working correctly");
                          }
                          println("outside of conditional");""",
            getParser());

    while (parser.hasNext()) {
      parser.next();
    }
  }

  @Test
  public void testInvalidConstReassign() {
    Parser parser =
        setParser("const myVar : string = 'Hello' + 2; myVar = 'Goodbye';", getParser());
    parser.next();
    assertThrows(InvalidConstReassignmentException.class, parser::next);
  }
}
