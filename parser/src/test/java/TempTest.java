import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import ast.identifier.Identifier;
import ast.literal.StringLiteral;
import ast.root.AstNodeType;
import ast.statements.VariableDeclaration;
import factory.LexerFactory;
import factory.ParserFactory;
import lexer.Lexer;
import org.junit.jupiter.api.Test;
import parsers.Parser;

public class TempTest {
  @Test
  public void varDecTest() {
    Identifier identifier = new Identifier("name", null, null);
    StringLiteral value = new StringLiteral("Oliver", null, null);
    VariableDeclaration variableDeclaration =
        new VariableDeclaration("let", identifier, value, "string", null, null);
    assertEquals("let", variableDeclaration.kind());
    assertEquals(AstNodeType.VARIABLE_DECLARATION, variableDeclaration.getNodeType());
    assertEquals(identifier, variableDeclaration.identifier());
    assertEquals(value, variableDeclaration.expression());
    variableDeclaration = new VariableDeclaration(identifier, value);
    assertNull(variableDeclaration.start());
    assertNull(variableDeclaration.end());
  }

  @Test
  public void visitorTest() {
    Lexer lexer = LexerFactory.getLexer("1.0");
    lexer = lexer.setInputAsString("let x : number = 1;");
    Parser parser = ParserFactory.getParser("1.0");
    parser.setLexer(lexer);
  }
}
