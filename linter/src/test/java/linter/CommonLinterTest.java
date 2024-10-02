package linter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import linter.engine.LinterEngine;
import org.junit.jupiter.api.Test;
import output.OutputReport;
import parsers.Parser;

public abstract class CommonLinterTest {
  protected abstract Linter getLinter();

  protected abstract Parser getParser(String code);

  @Test
  public void lintVariableDeclarationTest() {
    String code = "let snake_case: string = \"Oliver\";";
    Parser parser = getParser(code);

    Linter linter = getLinter();

    while (parser.hasNext()) {
      linter = linter.lint(parser.next());
    }

    LinterEngine engine = linter.engine();
    OutputReport output = (OutputReport) engine.getOutput();

    assertEquals(1, output.getFullReport().getReports().size());
  }

  @Test
  public void lintVariableDeclaration2Test() {
    String code = "let camelCase: string = \"Oliver\";";
    Parser parser = getParser(code);

    Linter linter = getLinter();

    while (parser.hasNext()) {
      linter = linter.lint(parser.next());
    }

    LinterEngine engine = linter.engine();
    OutputReport output = (OutputReport) engine.getOutput();

    assertEquals(0, output.getFullReport().getReports().size());
  }

  @Test
  public void lintVarDecWithBinaryExpressionTest() {
    String code = "let var: number = 1 + 1;";
    Parser parser = getParser(code);

    Linter linter = getLinter();

    while (parser.hasNext()) {
      linter = linter.lint(parser.next());
    }

    LinterEngine engine = linter.engine();
    OutputReport output = (OutputReport) engine.getOutput();

    assertEquals(0, output.getFullReport().getReports().size());
  }

  @Test
  public void lintCallExpressionTest() {
    String code = "println(\"Hello, World!\");";
    Parser parser = getParser(code);

    Linter linter = getLinter();

    while (parser.hasNext()) {
      linter = linter.lint(parser.next());
    }

    LinterEngine engine = linter.engine();
    OutputReport output = (OutputReport) engine.getOutput();

    assertEquals(0, output.getFullReport().getReports().size());
  }

  @Test
  public void lintCallExpression2Test() {
    String code = "let identifier: string = 'text';\nprintln(identifier);";
    Parser parser = getParser(code);

    Linter linter = getLinter();

    while (parser.hasNext()) {
      linter = linter.lint(parser.next());
    }

    LinterEngine engine = linter.engine();
    OutputReport output = (OutputReport) engine.getOutput();

    assertEquals(0, output.getFullReport().getReports().size());
  }

  @Test
  public void lintCallExpression3Test() {
    String code = "println(1 + 1);";
    Parser parser = getParser(code);

    Linter linter = getLinter();

    while (parser.hasNext()) {
      linter = linter.lint(parser.next());
    }

    LinterEngine engine = linter.engine();
    OutputReport output = (OutputReport) engine.getOutput();

    assertEquals(1, output.getFullReport().getReports().size());
  }
}
