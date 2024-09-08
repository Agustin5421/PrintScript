package linter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ast.literal.BooleanLiteral;
import ast.root.AstNode;
import ast.statements.IfStatement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LinterV1Test extends CommonLinterTest {
  private Linter linterV1;

  @BeforeEach
  public void setUp() {
    String rules = TestUtils.readResourceFile("linterRulesExampleReworkA.json");
    assertNotNull(rules);
    linterV1 = LinterFactory.getLinter("1.0", rules);
  }

  @Override
  protected Linter getLinter() {
    return linterV1;
  }

  @Test
  public void lintIfStatementTest() {
    AstNode ifNode = new IfStatement(null, null, null, null, null);
    Linter linter = getLinter();

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          linter.lint(ifNode);
        });
  }

  @Test
  public void lintBooleanLiteralTest() {
    AstNode booleanNode = new BooleanLiteral(true, null, null);
    Linter linter = getLinter();

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          linter.lint(booleanNode);
        });
  }
}
