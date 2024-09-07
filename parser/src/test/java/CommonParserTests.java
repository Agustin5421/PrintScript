import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import ast.expressions.BinaryExpression;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.VariableDeclaration;
import lexer.Lexer;
import org.junit.jupiter.api.Test;
import parsers.Parser;

public abstract class CommonParserTests {
  protected abstract Parser getParser();

  public Parser setParser(String input, Parser parser) {
    Lexer newLexer = parser.getLexer().setInput(input);
    return parser.setLexer(newLexer);
  }

  @Test
  public void testVariableDeclaration() {
    Parser parser = setParser("let name: string = \"Oliver\";", getParser());
    assertInstanceOf(VariableDeclaration.class, parser.next());
  }

  @Test
  public void testBinaryOperation() {
    Parser parser = setParser("myNumber =  1 + 'hola';", getParser());
    AssignmentExpression assignment = (AssignmentExpression) parser.next();

    assertInstanceOf(AssignmentExpression.class, assignment);

    BinaryExpression binary = (BinaryExpression) assignment.right();

    assertInstanceOf(BinaryExpression.class, binary);
    assertInstanceOf(NumberLiteral.class, binary.left());
    assertInstanceOf(StringLiteral.class, binary.right());
  }

  @Test
  public void testCallFunction() {
    Parser parser = setParser("println(2);", getParser());
    CallExpression call = (CallExpression) parser.next();
    assertInstanceOf(CallExpression.class, call);
    assertInstanceOf(NumberLiteral.class, call.arguments().get(0));
  }

  @Test
  public void testCallFunction2() {
    Parser parser = setParser("println(2, 2);", getParser());
    CallExpression call = (CallExpression) parser.next();
    assertInstanceOf(CallExpression.class, call);
    assertInstanceOf(NumberLiteral.class, call.arguments().get(0));
    assertInstanceOf(NumberLiteral.class, call.arguments().get(1));
  }

  @Test
  public void testCallFunction3() {
    Parser parser = setParser("println(2 + 2);", getParser());
    CallExpression call = (CallExpression) parser.next();
    assertInstanceOf(CallExpression.class, call);
    assertInstanceOf(BinaryExpression.class, call.arguments().get(0));
  }
}
