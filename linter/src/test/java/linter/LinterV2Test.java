package linter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ast.expressions.ExpressionNode;
import ast.literal.BooleanLiteral;
import ast.root.AstNode;
import ast.statements.IfStatement;
import java.util.List;
import linter.visitor.strategy.NewLinterVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import output.OutputListString;
import parsers.Parser;

public class LinterV2Test extends CommonLinterTest {
  private ReworkedLinter linterV2;

  @BeforeEach
  public void setUp() {
    String rules = TestUtils.readResourceFile("linterRulesRework2A.json");
    assertNotNull(rules);
    linterV2 = LinterFactory.getReworkedLinter("1.1", rules, new OutputListString());
  }

  @Override
  protected ReworkedLinter getLinter() {
    return linterV2;
  }

  @Test
  public void lintIfStatementTest() {
    ExpressionNode booleanNode = new BooleanLiteral(true, null, null);
    AstNode ifNode = new IfStatement(null, null, booleanNode, List.of(), List.of());
    ReworkedLinter linter = getLinter();

    linter = linter.lint(ifNode);
    NewLinterVisitor visitor = linter.getVisitor();
    OutputListString output = (OutputListString) visitor.getOutput();

    assertEquals(0, output.getSavedResults().size());
  }

  @Test
  public void lintBooleanLiteralTest() {
    AstNode booleanNode = new BooleanLiteral(true, null, null);
    ReworkedLinter linter = getLinter();

    linter = linter.lint(booleanNode);
    NewLinterVisitor visitor = linter.getVisitor();
    OutputListString output = (OutputListString) visitor.getOutput();

    assertEquals(0, output.getSavedResults().size());
  }

  @Test
  public void emptyConfigTest() {
    ReworkedLinter linter = LinterFactory.getReworkedLinter("1.1", "{}", new OutputListString());
    String code = "let snake_case: string = \"Oliver\"; let camelCase: string = \"Oliver\";";
    Parser parser = getParser(code);

    while (parser.hasNext()) {
      linter = linter.lint(parser.next());
    }

    NewLinterVisitor visitor = linter.getVisitor();
    OutputListString output = (OutputListString) visitor.getOutput();

    assertEquals(0, output.getSavedResults().size());
  }
}
