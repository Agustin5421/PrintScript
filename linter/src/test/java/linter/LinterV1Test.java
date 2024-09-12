package linter;

import static org.junit.jupiter.api.Assertions.*;

import ast.literal.BooleanLiteral;
import ast.root.AstNode;
import ast.statements.IfStatement;
import lexer.Lexer;
import linter.visitor.report.FullReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parsers.Parser;

public class LinterV1Test extends CommonLinterTest {
  private Linter linterV1;

  @BeforeEach
  public void setUp() {
    String rules = TestUtils.readResourceFile("linterRulesRework2A.json");
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

  @Test
  public void emptyConfigTest() {
    Linter linter = LinterFactory.getLinter("1.0", "{}");
    String code = "let snake_case: string = \"Oliver\"; let camelCase: string = \"Oliver\";";

    Parser parser = linter.getParser();
    Lexer newLexer = parser.getLexer().setInputAsString(code);
    linter = linter.setParser(parser.setLexer(newLexer));

    FullReport report = new FullReport();
    while (linter.hasNext()) {
      report = linter.next();
    }

    assertEquals(0, report.getReports().size());
  }
}
