package linter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ast.expressions.ExpressionNode;
import ast.literal.BooleanLiteral;
import ast.root.AstNode;
import ast.statements.IfStatement;
import factory.ParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import lexer.Lexer;
import linter.engine.LinterEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import output.OutputListString;
import parsers.Parser;

public class LinterV2Test extends CommonLinterTest {
  private Linter linterV2;
  private Parser parser;

  @BeforeEach
  public void setUp() {
    String rules = TestUtils.readResourceFile("linterRulesRework2A.json");
    assertNotNull(rules);
    linterV2 = LinterFactory.getReworkedLinter("1.1", rules, new OutputListString());

    parser = ParserFactory.getParser("1.1");
  }

  protected Parser getParser(String code) {
    Lexer lexer = parser.getLexer();
    try {
      lexer = lexer.setInput(new ByteArrayInputStream(code.getBytes()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return parser.setLexer(lexer);
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

    linter = linter.lint(ifNode);
    LinterEngine engine = linter.engine();
    OutputListString output = (OutputListString) engine.getOutput();

    assertEquals(0, output.getSavedResults().size());
  }

  @Test
  public void lintBooleanLiteralTest() {
    AstNode booleanNode = new BooleanLiteral(true, null, null);
    Linter linter = getLinter();

    linter = linter.lint(booleanNode);
    LinterEngine engine = linter.engine();
    OutputListString output = (OutputListString) engine.getOutput();

    assertEquals(0, output.getSavedResults().size());
  }

  @Test
  public void emptyConfigTest() {
    Linter linter = LinterFactory.getReworkedLinter("1.1", "{}", new OutputListString());
    String code = "let snake_case: string = \"Oliver\"; let camelCase: string = \"Oliver\";";
    Parser parser = getParser(code);

    while (parser.hasNext()) {
      linter = linter.lint(parser.next());
    }

    LinterEngine engine = linter.engine();
    OutputListString output = (OutputListString) engine.getOutput();

    assertEquals(0, output.getSavedResults().size());
  }

  @Test
  public void lintIfCodeTest() {
    String code = "if (true) { let a: number = 1; }";
    Parser parser = getParser(code);

    Linter linter = getLinter();

    while (parser.hasNext()) {
      AstNode next = parser.next();
      linter = linter.lint(next);
    }

    LinterEngine engine = linterV2.engine();
    OutputListString output = (OutputListString) engine.getOutput();

    assertEquals(0, output.getSavedResults().size());
  }
}
