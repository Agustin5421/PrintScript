package linter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import linter.visitor.strategy.NewLinterVisitor;
import org.junit.jupiter.api.Test;
import output.OutputListString;
import parsers.Parser;

public abstract class CommonLinterTest {
  protected abstract ReworkedLinter getLinter();

  protected abstract Parser getParser(String code);

  @Test
  public void lintVariableDeclarationTest() {
    String code = "let snake_case: string = \"Oliver\";";
    Parser parser = getParser(code);

    ReworkedLinter linter = getLinter();

    while (parser.hasNext()) {
      linter = linter.lint(parser.next());
    }

    NewLinterVisitor visitor = linter.getVisitor();
    OutputListString output = (OutputListString) visitor.getOutput();

    assertEquals(1, output.getSavedResults().size());
  }

  @Test
  public void lintVariableDeclaration2Test() {
    String code = "let camelCase: string = \"Oliver\";";
    Parser parser = getParser(code);

    ReworkedLinter linter = getLinter();

    while (parser.hasNext()) {
      linter = linter.lint(parser.next());
    }

    NewLinterVisitor visitor = linter.getVisitor();
    OutputListString output = (OutputListString) visitor.getOutput();

    assertEquals(0, output.getSavedResults().size());
  }

  @Test
  public void lintVarDecWithBinaryExpressionTest() {
    String code = "let var: number = 1 + 1;";
    Parser parser = getParser(code);

    ReworkedLinter linter = getLinter();

    while (parser.hasNext()) {
      linter = linter.lint(parser.next());
    }

    NewLinterVisitor visitor = linter.getVisitor();
    OutputListString output = (OutputListString) visitor.getOutput();

    assertEquals(0, output.getSavedResults().size());
  }

  @Test
  public void lintCallExpressionTest() {
    String code = "println(\"Hello, World!\");";
    Parser parser = getParser(code);

    ReworkedLinter linter = getLinter();

    while (parser.hasNext()) {
      linter = linter.lint(parser.next());
    }

    NewLinterVisitor visitor = linter.getVisitor();
    OutputListString output = (OutputListString) visitor.getOutput();

    assertEquals(0, output.getSavedResults().size());
  }

  @Test
  public void lintCallExpression2Test() {
    String code = "let identifier: string = 'text';\nprintln(identifier);";
    Parser parser = getParser(code);

    ReworkedLinter linter = getLinter();

    while (parser.hasNext()) {
      linter = linter.lint(parser.next());
    }

    NewLinterVisitor visitor = linter.getVisitor();
    OutputListString output = (OutputListString) visitor.getOutput();

    assertEquals(0, output.getSavedResults().size());
  }

  @Test
  public void lintCallExpression3Test() {
    String code = "println(1 + 1);";
    Parser parser = getParser(code);

    ReworkedLinter linter = getLinter();

    while (parser.hasNext()) {
      linter = linter.lint(parser.next());
    }

    NewLinterVisitor visitor = linter.getVisitor();
    OutputListString output = (OutputListString) visitor.getOutput();

    assertEquals(1, output.getSavedResults().size());
  }
}
