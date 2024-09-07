package linter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lexer.Lexer;
import linter.report.FullReport;
import org.junit.jupiter.api.Test;
import parsers.Parser;

public abstract class CommonLinterTest {
  protected abstract IterableLinter getLinter();

  private IterableLinter configureLinter(String code) {
    IterableLinter linter = getLinter();
    Parser parser = linter.getParser();
    Lexer newLexer = parser.getLexer().setInput(code);
    return linter.setParser(parser.setLexer(newLexer));
  }

  @Test
  public void lintVariableDeclarationTest() {
    String code = "let snake_case: string = \"Oliver\";";
    IterableLinter linter = configureLinter(code);
    FullReport report = linter.next();
    assertEquals(1, report.getReports().size());
  }

  @Test
  public void lintVariableDeclaration2Test() {
    String code = "let camelCase: string = \"Oliver\";";
    IterableLinter linter = configureLinter(code);
    FullReport report = linter.next();
    assertEquals(0, report.getReports().size());
  }

  @Test
  public void lintVarDecWithBinaryExpressionTest() {
    String code = "let var: Number = 1 + 1;";
    IterableLinter linter = configureLinter(code);
    FullReport report = linter.next();
    assertEquals(0, report.getReports().size());
  }

  @Test
  public void lintCallExpressionTest() {
    String code = "println(\"Hello, World!\");";
    IterableLinter linter = configureLinter(code);
    FullReport report = linter.next();
    assertEquals(0, report.getReports().size());
  }

  @Test
  public void lintCallExpression2Test() {
    String code = "println(identifier);";
    IterableLinter linter = configureLinter(code);
    FullReport report = linter.next();
    assertEquals(0, report.getReports().size());
  }

  @Test
  public void lintCallExpression3Test() {
    String code = "println(1 + 1);";
    IterableLinter linter = configureLinter(code);
    FullReport report = linter.next();
    assertEquals(1, report.getReports().size());
  }
}
