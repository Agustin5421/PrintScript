package interpreter.rework;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ast.identifier.Identifier;
import ast.literal.BooleanLiteral;
import ast.literal.StringLiteral;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.IfStatement;
import ast.statements.VariableDeclaration;
import interpreter.visitor.InterpreterVisitorV3;
import interpreter.visitor.factory.InterpreterVisitorV3Factory;
import interpreter.visitor.repository.VariableIdentifier;
import interpreter.visitor.repository.VariableIdentifierFactory;
import interpreter.visitor.staticprovider.Inputs;
import java.util.ArrayDeque;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import output.OutputListString;
import token.Position;

public class VisitorV2Test extends CommonVisitorV3Test {
  private InterpreterVisitorV3 visitor;

  @BeforeEach
  public void setUp() {
    visitor = InterpreterVisitorV3Factory.getVisitor("1.1", new OutputListString());
  }

  @Override
  protected InterpreterVisitorV3 getVisitor() {
    return visitor;
  }

  @Test
  public void interpretIfStatementInsideThen() {
    Position position = new Position(0, 0);
    BooleanLiteral condition = new BooleanLiteral(true, position, position);

    Identifier methodName = new Identifier("println", position, position);
    StringLiteral stringLiteral = new StringLiteral("inside then block", position, position);
    CallExpression thenCall =
        new CallExpression(methodName, List.of(stringLiteral), position, position);

    StringLiteral stringLiteral2 = new StringLiteral("inside else block", position, position);
    CallExpression elseCall =
        new CallExpression(methodName, List.of(stringLiteral2), position, position);

    IfStatement ifStatement =
        new IfStatement(position, position, condition, List.of(thenCall), List.of(elseCall));

    InterpreterVisitorV3 visitor = getVisitor();
    visitor.visit(ifStatement);

    List<String> output = ((OutputListString) visitor.getOutputResult()).getSavedResults();
    assertEquals(1, output.size());
    assertEquals("inside then block", output.get(0));
  }

  @Test
  public void interpretIfStatementInsideElse() {
    Position position = new Position(0, 0);
    BooleanLiteral condition = new BooleanLiteral(false, position, position);

    Identifier methodName = new Identifier("println", position, position);
    StringLiteral stringLiteral = new StringLiteral("inside then block", position, position);
    CallExpression thenCall =
        new CallExpression(methodName, List.of(stringLiteral), position, position);

    StringLiteral stringLiteral2 = new StringLiteral("inside else block", position, position);
    CallExpression elseCall =
        new CallExpression(methodName, List.of(stringLiteral2), position, position);

    IfStatement ifStatement =
        new IfStatement(position, position, condition, List.of(thenCall), List.of(elseCall));

    InterpreterVisitorV3 visitor = getVisitor();
    visitor.visit(ifStatement);

    List<String> output = ((OutputListString) visitor.getOutputResult()).getSavedResults();
    assertEquals(1, output.size());
    assertEquals("inside else block", output.get(0));
  }

  @Test
  public void interpretIfStatementNoElse() {
    Position position = new Position(0, 0);
    BooleanLiteral condition = new BooleanLiteral(false, position, position);

    Identifier methodName = new Identifier("println", position, position);
    StringLiteral stringLiteral = new StringLiteral("inside then block", position, position);
    CallExpression thenCall =
        new CallExpression(methodName, List.of(stringLiteral), position, position);

    IfStatement ifStatement =
        new IfStatement(position, position, condition, List.of(thenCall), List.of());

    InterpreterVisitorV3 visitor = getVisitor();
    visitor.visit(ifStatement);

    List<String> output = ((OutputListString) visitor.getOutputResult()).getSavedResults();
    assertEquals(0, output.size());
  }

  @Test
  public void interpretBooleanLiteral() {
    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("x", position, position);
    BooleanLiteral booleanLiteral = new BooleanLiteral(true, position, position);
    VariableDeclaration variableDeclaration =
        new VariableDeclaration("let", identifier, booleanLiteral, "boolean", position, position);

    InterpreterVisitorV3 visitor = getVisitor();
    visitor = (InterpreterVisitorV3) visitor.visit(variableDeclaration);

    VariableIdentifier variableIdentifier =
        VariableIdentifierFactory.createVarIdFromIdentifier(identifier);
    assertEquals(true, visitor.getVariablesRepository().getNewVariable(variableIdentifier).value());
  }

  @Test
  public void interpretConstVariable() {
    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("x", position, position);
    BooleanLiteral booleanLiteral = new BooleanLiteral(true, position, position);
    VariableDeclaration variableDeclaration =
        new VariableDeclaration("const", identifier, booleanLiteral, "string", position, position);

    InterpreterVisitorV3 visitor = getVisitor();
    visitor = (InterpreterVisitorV3) visitor.visit(variableDeclaration);

    VariableIdentifier variableIdentifier =
        VariableIdentifierFactory.createVarIdFromIdentifier(identifier);
    assertEquals(true, visitor.getVariablesRepository().getNewVariable(variableIdentifier).value());

    BooleanLiteral newBooleanLiteral = new BooleanLiteral(false, position, position);
    AssignmentExpression assignmentExpression =
        new AssignmentExpression(identifier, newBooleanLiteral, "=");

    InterpreterVisitorV3 finalVisitor = visitor;
    assertThrows(Exception.class, () -> finalVisitor.visit(assignmentExpression));
  }

  @Test
  public void interpretReadInput() {
    Position position = new Position(0, 0);
    Identifier methodName = new Identifier("readInput", position, position);
    StringLiteral stringLiteral = new StringLiteral("Sample text", position, position);
    CallExpression inputCall =
        new CallExpression(methodName, List.of(stringLiteral), position, position);
    Identifier identifier = new Identifier("x", position, position);
    VariableDeclaration variableDeclaration =
        new VariableDeclaration("let", identifier, inputCall, "string", position, position);

    String input = "hello";
    Inputs.setInputs(null);
    Inputs.setInputs(new ArrayDeque<>(List.of(input)));

    InterpreterVisitorV3 visitor = getVisitor();
    visitor = (InterpreterVisitorV3) visitor.visit(variableDeclaration);

    VariableIdentifier variableIdentifier =
        VariableIdentifierFactory.createVarIdFromIdentifier(identifier);
    assertEquals(
        input, visitor.getVariablesRepository().getNewVariable(variableIdentifier).value());

    OutputListString outputResult = (OutputListString) visitor.getOutputResult();
    assertEquals(1, outputResult.getSavedResults().size());
    assertEquals("Sample text", outputResult.getSavedResults().get(0));

    Inputs.setInputs(null);
  }

  @Test
  public void failReadInputNoInput() {
    Position position = new Position(0, 0);
    Identifier methodName = new Identifier("readInput", position, position);
    StringLiteral stringLiteral = new StringLiteral("Sample text", position, position);
    CallExpression inputCall =
        new CallExpression(methodName, List.of(stringLiteral), position, position);
    Identifier identifier = new Identifier("x", position, position);
    VariableDeclaration variableDeclaration =
        new VariableDeclaration("let", identifier, inputCall, "string", position, position);

    Inputs.setInputs(null);

    InterpreterVisitorV3 visitor = getVisitor();
    assertThrows(Exception.class, () -> visitor.visit(variableDeclaration));
  }

  @Test
  public void interpretReadEnv() {
    Position position = new Position(0, 0);
    Identifier methodName = new Identifier("readEnv", position, position);
    StringLiteral stringLiteral = new StringLiteral("UNIVERSAL_CONSTANT", position, position);
    CallExpression inputCall =
        new CallExpression(methodName, List.of(stringLiteral), position, position);
    Identifier identifier = new Identifier("x", position, position);
    VariableDeclaration variableDeclaration =
        new VariableDeclaration("let", identifier, inputCall, "string", position, position);

    InterpreterVisitorV3 visitor = getVisitor();
    visitor = (InterpreterVisitorV3) visitor.visit(variableDeclaration);

    VariableIdentifier variableIdentifier =
        VariableIdentifierFactory.createVarIdFromIdentifier(identifier);
    assertEquals(
        "constant", visitor.getVariablesRepository().getNewVariable(variableIdentifier).value());
  }

  @Test
  public void failReadEnvNoEnv() {
    Position position = new Position(0, 0);
    Identifier methodName = new Identifier("readEnv", position, position);
    StringLiteral stringLiteral = new StringLiteral("NON_ENV", position, position);
    CallExpression inputCall =
        new CallExpression(methodName, List.of(stringLiteral), position, position);
    Identifier identifier = new Identifier("x", position, position);
    VariableDeclaration variableDeclaration =
        new VariableDeclaration("let", identifier, inputCall, "string", position, position);

    InterpreterVisitorV3 visitor = getVisitor();
    assertThrows(Exception.class, () -> visitor.visit(variableDeclaration));
  }
}
