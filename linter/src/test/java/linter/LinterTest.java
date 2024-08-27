package linter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.root.AstNode;
import ast.root.Program;
import ast.statements.CallExpression;
import ast.statements.VariableDeclaration;
import java.util.List;
import org.junit.jupiter.api.Test;
import token.Position;

public class LinterTest {
  private final Position defaultPosition = new Position(0, 0);

  @Test
  public void testIdentifierRuleIsMet() {
    Linter linter = LinterInitializer.initLinter();
    String rules = TestUtils.readResourceFile("linterRulesExample.json");
    assertNotNull(rules);

    Identifier identifier = new Identifier("testName", defaultPosition, defaultPosition);
    StringLiteral stringLiteral = new StringLiteral("testValue", defaultPosition, defaultPosition);
    VariableDeclaration variableDeclaration = new VariableDeclaration(identifier, stringLiteral);
    Program program = new Program(List.of(variableDeclaration));

    String report = linter.linter(program, rules);
    assertEquals("", report);
  }

  @Test
  public void testIdentifierRuleIsNotMet() {
    Linter linter = LinterInitializer.initLinter();
    String rules = TestUtils.readResourceFile("linterRulesExample.json");
    assertNotNull(rules);

    Identifier identifier = new Identifier("test_name", defaultPosition, defaultPosition);
    StringLiteral stringLiteral = new StringLiteral("testValue", defaultPosition, defaultPosition);
    VariableDeclaration variableDeclaration = new VariableDeclaration(identifier, stringLiteral);
    Program program = new Program(List.of(variableDeclaration));

    String report = linter.linter(program, rules);
    assertEquals(
        "Warning from (row: 0, col: 0) to (row: 0, col: 0): Identifier test_name does not follow camelCase convention",
        report);
  }

  @Test
  public void testIdentifierRuleIsNotMet2() {
    Linter linter = LinterInitializer.initLinter();
    String rules = TestUtils.readResourceFile("linterRulesExample.json");
    assertNotNull(rules);

    Identifier identifier = new Identifier("TestName", defaultPosition, defaultPosition);
    StringLiteral stringLiteral = new StringLiteral("testValue", defaultPosition, defaultPosition);
    VariableDeclaration variableDeclaration = new VariableDeclaration(identifier, stringLiteral);
    Program program = new Program(List.of(variableDeclaration));

    String report = linter.linter(program, rules);
    assertEquals(
        "Warning from (row: 0, col: 0) to (row: 0, col: 0): Identifier TestName does not follow camelCase convention",
        report);
  }

  @Test
  public void testArgumentsRuleIsMet() {
    Linter linter = LinterInitializer.initLinter();
    String rules = TestUtils.readResourceFile("linterRulesExample.json");
    assertNotNull(rules);

    NumberLiteral number = new NumberLiteral(1, defaultPosition, defaultPosition);

    Identifier method = new Identifier("println", defaultPosition, defaultPosition);
    List<AstNode> arguments = List.of(number);
    CallExpression callExpression = new CallExpression(method, arguments, false);
    Program program = new Program(List.of(callExpression));

    String report = linter.linter(program, rules);

    assertEquals("", report);
  }

  @Test
  public void testArgumentsRuleIsNotMet() {
    Linter linter = LinterInitializer.initLinter();
    String rules = TestUtils.readResourceFile("linterRulesExample.json");
    assertNotNull(rules);

    NumberLiteral left = new NumberLiteral(1, defaultPosition, defaultPosition);
    NumberLiteral right = new NumberLiteral(2, defaultPosition, defaultPosition);
    BinaryExpression binaryExpression = new BinaryExpression(left, right, "+");

    Identifier method = new Identifier("println", defaultPosition, defaultPosition);
    List<AstNode> arguments = List.of(binaryExpression);
    CallExpression callExpression = new CallExpression(method, arguments, false);
    Program program = new Program(List.of(callExpression));

    String report = linter.linter(program, rules);
    assertEquals(
        "Warning from (row: 0, col: 0) to (row: 0, col: 0): Expression is not allowed as CallExpression argument",
        report);
  }

  @Test
  public void testRulesNotMet() {
    Linter linter = LinterInitializer.initLinter();
    String rules = TestUtils.readResourceFile("linterRulesExample.json");
    assertNotNull(rules);

    Identifier identifier = new Identifier("test_name", defaultPosition, defaultPosition);
    StringLiteral stringLiteral = new StringLiteral("testValue", defaultPosition, defaultPosition);
    VariableDeclaration variableDeclaration = new VariableDeclaration(identifier, stringLiteral);

    NumberLiteral left = new NumberLiteral(1, defaultPosition, defaultPosition);
    NumberLiteral right = new NumberLiteral(2, defaultPosition, defaultPosition);
    BinaryExpression binaryExpression = new BinaryExpression(left, right, "+");

    Identifier method = new Identifier("println", defaultPosition, defaultPosition);
    List<AstNode> arguments = List.of(binaryExpression);
    CallExpression callExpression = new CallExpression(method, arguments, false);

    Program program = new Program(List.of(variableDeclaration, callExpression));

    String report = linter.linter(program, rules);
    assertEquals(
        "Warning from (row: 0, col: 0) to (row: 0, col: 0): Identifier test_name does not follow camelCase convention\n"
            + "Warning from (row: 0, col: 0) to (row: 0, col: 0): Expression is not allowed as CallExpression argument",
        report);
  }
}
