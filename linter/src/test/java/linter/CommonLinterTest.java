package linter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lexer.Lexer;
import linter.visitor.report.FullReport;
import org.junit.jupiter.api.Test;
import parsers.Parser;

public abstract class CommonLinterTest {
  protected abstract Linter getLinter();

  private Linter configureLinter(String code) {
    Linter linter = getLinter();
    Parser parser = linter.getParser();
    Lexer newLexer = parser.getLexer().setInput(code);
    return linter.setParser(parser.setLexer(newLexer));
  }

  @Test
  public void lintVariableDeclarationTest() {
    String code = "let snake_case: string = \"Oliver\";";
    Linter linter = configureLinter(code);
    FullReport report = linter.next();
    assertEquals(1, report.getReports().size());
  }

  @Test
  public void lintVariableDeclaration2Test() {
    String code = "let camelCase: string = \"Oliver\";";
    Linter linter = configureLinter(code);
    FullReport report = linter.next();
    assertEquals(0, report.getReports().size());
  }

  @Test
  public void lintVarDecWithBinaryExpressionTest() {
    String code = "let var: number = 1 + 1;";
    Linter linter = configureLinter(code);
    FullReport report = linter.next();
    assertEquals(0, report.getReports().size());
  }

  @Test
  public void lintCallExpressionTest() {
    String code = "println(\"Hello, World!\");";
    Linter linter = configureLinter(code);
    FullReport report = linter.next();
    assertEquals(0, report.getReports().size());
  }

  @Test
  public void lintCallExpression2Test() {
    String code = "println(identifier);";
    Linter linter = configureLinter(code);
    FullReport report = linter.next();
    assertEquals(0, report.getReports().size());
  }

  @Test
  public void lintCallExpression3Test() {
    String code = "println(1 + 1);";
    Linter linter = configureLinter(code);
    FullReport report = linter.next();
    assertEquals(1, report.getReports().size());
  }
}
