package linter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ast.literal.BooleanLiteral;
import ast.root.AstNode;
import ast.statements.IfStatement;
import factory.ParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import lexer.Lexer;
import linter.visitor.strategy.NewLinterVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import output.OutputListString;
import parsers.Parser;

public class LinterV1Test extends CommonLinterTest {
  private ReworkedLinter linterV1;
  private Parser parser;

  @BeforeEach
  public void setUp() {
    String rules = TestUtils.readResourceFile("linterRulesRework2A.json");
    assertNotNull(rules);
    linterV1 = LinterFactory.getReworkedLinter("1.0", rules, new OutputListString());

    parser = ParserFactory.getParser("1.0");
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
  protected ReworkedLinter getLinter() {
    return linterV1;
  }

  @Test
  public void lintIfStatementTest() {
    AstNode ifNode = new IfStatement(null, null, null, null, null);
    ReworkedLinter linter = getLinter();

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          linter.lint(ifNode);
        });
  }

  @Test
  public void lintBooleanLiteralTest() {
    AstNode booleanNode = new BooleanLiteral(true, null, null);
    ReworkedLinter linter = getLinter();

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          linter.lint(booleanNode);
        });
  }

  @Test
  public void emptyConfigTest() {
    ReworkedLinter linter = LinterFactory.getReworkedLinter("1.0", "{}", new OutputListString());
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
