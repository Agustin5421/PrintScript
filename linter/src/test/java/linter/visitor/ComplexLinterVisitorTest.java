package linter.visitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ast.identifier.Identifier;
import ast.literal.StringLiteral;
import ast.root.Program;
import ast.statements.CallExpression;
import ast.statements.VariableDeclaration;
import java.util.ArrayList;
import java.util.List;
import linter.Linter;
import linter.TestUtils;
import linter.report.FullReport;
import observers.Observer;
import observers.ProgressObserver;
import observers.ProgressPrinter;
import org.junit.jupiter.api.Test;
import token.Position;

public class ComplexLinterVisitorTest {
  private Program getProgram() {
    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("testName", position, position);
    StringLiteral stringLiteral = new StringLiteral("test", position, position);
    VariableDeclaration variableDeclaration =
        // TODO: Fix this
        new VariableDeclaration("test", identifier, stringLiteral, "test", position, position);

    CallExpression callExpression =
        new CallExpression(identifier, List.of(identifier, stringLiteral));

    return new Program(List.of(variableDeclaration, callExpression));
  }

  private Linter getLinter() {
    Observer observer = new ProgressObserver(new ProgressPrinter(), 3);
    return new Linter(new ArrayList<>(List.of(observer)));
  }

  @Test
  public void noViolationsTest() {
    Program program = getProgram();

    String rules = TestUtils.readResourceFile("linterRulesExampleReworkA.json");
    assertNotNull(rules);

    Linter linter = getLinter();
    FullReport report = linter.lint(program, rules);

    assertEquals(0, report.getReports().size());
  }

  @Test
  public void violationsTest() {
    Program program = getProgram();

    String rules = TestUtils.readResourceFile("linterRulesExampleReworkB.json");
    assertNotNull(rules);

    Linter linter = getLinter();
    FullReport report = linter.lint(program, rules);

    assertEquals(4, report.getReports().size());
  }
}
