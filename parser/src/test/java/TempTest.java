import static org.junit.jupiter.api.Assertions.*;

import ast.identifier.Identifier;
import ast.literal.StringLiteral;
import ast.root.AstNodeType;
import ast.statements.ConstDeclaration;
import ast.statements.VariableDeclaration;
import org.junit.jupiter.api.Test;

public class TempTest {
  @Test
  public void constDeclarationTest() {
    Identifier identifier = new Identifier("name", null, null);
    StringLiteral value = new StringLiteral("Oliver", null, null);
    ConstDeclaration constDeclaration = new ConstDeclaration(identifier, value, null, null);
    assertEquals(AstNodeType.VARIABLE_DECLARATION, constDeclaration.getNodeType());
    assertEquals(identifier, constDeclaration.identifier());
    assertEquals(value, constDeclaration.expression());
    assertNull(constDeclaration.start());
    assertNull(constDeclaration.end());
    constDeclaration = new ConstDeclaration(identifier, value);
    ConstDeclaration finalConstDeclaration = constDeclaration;
    assertThrows(
        UnsupportedOperationException.class,
        () -> {
          finalConstDeclaration.accept(null);
        });
  }

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
}
