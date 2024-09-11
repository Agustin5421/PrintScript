package linter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ast.expressions.ExpressionNode;
import ast.literal.BooleanLiteral;
import ast.root.AstNode;
import ast.statements.IfStatement;
import java.util.List;
import lexer.Lexer;
import linter.visitor.report.FullReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parsers.Parser;

public class LinterV2Test extends CommonLinterTest {
  private Linter linterV2;

  @BeforeEach
  public void setUp() {
    String rules = TestUtils.readResourceFile("linterRulesRework2A.json");
    assertNotNull(rules);
    linterV2 = LinterFactory.getLinter("1.1", rules);
  }

  @Override
  protected Linter getLinter() {
    return linterV2;
  }

  @Test
  public void lintIfStatementTest() {
    ExpressionNode booleanNode = new BooleanLiteral(true, null, null);
    AstNode ifNode = new IfStatement(null, null, booleanNode, List.of(), List.of());
    Linter linter = getLinter();

    FullReport report = linter.lint(ifNode);
    assertEquals(0, report.getReports().size());
  }

  @Test
  public void lintBooleanLiteralTest() {
    AstNode booleanNode = new BooleanLiteral(true, null, null);
    Linter linter = getLinter();

    FullReport report = linter.lint(booleanNode);
    assertEquals(0, report.getReports().size());
  }

  @Test
  public void emptyConfigTest() {
    Linter linter = LinterFactory.getLinter("1.1", "{}");
    String code = "let snake_case: string = \"Oliver\"; let camelCase: string = \"Oliver\";";

    Parser parser = linter.getParser();
    Lexer newLexer = parser.getLexer().setInput(code);
    linter = linter.setParser(parser.setLexer(newLexer));

    FullReport report = new FullReport();
    while (linter.hasNext()) {
      report = linter.next();
    }

    assertEquals(0, report.getReports().size());
  }
}
