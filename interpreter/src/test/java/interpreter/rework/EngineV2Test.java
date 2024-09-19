package interpreter.rework;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.BooleanLiteral;
import ast.literal.StringLiteral;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.IfStatement;
import ast.statements.VariableDeclaration;
import input.InputQueue;
import interpreter.engine.InterpreterEngine;
import interpreter.engine.factory.InterpreterEngineFactory;
import interpreter.engine.repository.VariableIdentifier;
import interpreter.engine.repository.VariableIdentifierFactory;
import interpreter.engine.staticprovider.Inputs;
import java.util.ArrayDeque;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import output.OutputListString;
import token.Position;

public class EngineV2Test extends CommonEngineTest {
  private InterpreterEngine engine;

  @BeforeEach
  public void setUp() {
    engine = InterpreterEngineFactory.getEngine("1.1", new OutputListString());
  }

  @Override
  protected InterpreterEngine getEngine() {
    return engine;
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

    InterpreterEngine visitor = getEngine();
    visitor.interpret(ifStatement);

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

    InterpreterEngine visitor = getEngine();
    visitor.interpret(ifStatement);

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

    InterpreterEngine visitor = getEngine();
    visitor.interpret(ifStatement);

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

    InterpreterEngine engine = getEngine();
    engine = engine.interpret(variableDeclaration);

    VariableIdentifier variableIdentifier =
        VariableIdentifierFactory.createVarIdFromIdentifier(identifier);
    assertEquals(true, engine.getVariablesRepository().getNewVariable(variableIdentifier).value());
  }

  @Test
  public void interpretConstVariable() {
    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("x", position, position);
    BooleanLiteral booleanLiteral = new BooleanLiteral(true, position, position);
    VariableDeclaration variableDeclaration =
        new VariableDeclaration("const", identifier, booleanLiteral, "string", position, position);

    InterpreterEngine engine = getEngine();
    engine = engine.interpret(variableDeclaration);

    VariableIdentifier variableIdentifier =
        VariableIdentifierFactory.createVarIdFromIdentifier(identifier);
    assertEquals(true, engine.getVariablesRepository().getNewVariable(variableIdentifier).value());

    BooleanLiteral newBooleanLiteral = new BooleanLiteral(false, position, position);
    AssignmentExpression assignmentExpression =
        new AssignmentExpression(identifier, newBooleanLiteral, "=");

    InterpreterEngine finalVisitor = engine;
    assertThrows(Exception.class, () -> finalVisitor.interpret(assignmentExpression));
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
    Inputs.setInputs(new InputQueue(new ArrayDeque<>(List.of(input))));

    InterpreterEngine engine = getEngine();
    engine = engine.interpret(variableDeclaration);

    VariableIdentifier variableIdentifier =
        VariableIdentifierFactory.createVarIdFromIdentifier(identifier);
    assertEquals(input, engine.getVariablesRepository().getNewVariable(variableIdentifier).value());

    OutputListString outputResult = (OutputListString) engine.getOutputResult();
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

    InterpreterEngine visitor = getEngine();
    assertThrows(Exception.class, () -> visitor.interpret(variableDeclaration));
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

    InterpreterEngine engine = getEngine();
    engine = engine.interpret(variableDeclaration);

    VariableIdentifier variableIdentifier =
        VariableIdentifierFactory.createVarIdFromIdentifier(identifier);
    assertEquals(
        "constant", engine.getVariablesRepository().getNewVariable(variableIdentifier).value());
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

    InterpreterEngine visitor = getEngine();
    assertThrows(Exception.class, () -> visitor.interpret(variableDeclaration));
  }

  @Test
  public void binaryExpressionWithReadInput() {
    Position position = new Position(0, 0);
    Identifier methodName = new Identifier("readInput", position, position);
    StringLiteral stringLiteral = new StringLiteral("Sample text", position, position);
    CallExpression inputCall =
        new CallExpression(methodName, List.of(stringLiteral), position, position);

    Identifier identifier = new Identifier("x", position, position);
    StringLiteral stringLiteral2 = new StringLiteral("Hello ", position, position);
    BinaryExpression binaryExpression = new BinaryExpression(stringLiteral2, inputCall, "+");
    VariableDeclaration variableDeclaration =
        new VariableDeclaration("let", identifier, binaryExpression, "string", position, position);

    String input = "world";
    Inputs.setInputs(null);
    Inputs.setInputs(new InputQueue(new ArrayDeque<>(List.of(input))));

    InterpreterEngine engine = getEngine();
    engine = engine.interpret(variableDeclaration);

    VariableIdentifier variableIdentifier =
        VariableIdentifierFactory.createVarIdFromIdentifier(identifier);
    assertEquals(
        "Hello world", engine.getVariablesRepository().getNewVariable(variableIdentifier).value());

    OutputListString outputResult = (OutputListString) engine.getOutputResult();
    assertEquals(1, outputResult.getSavedResults().size());
    assertEquals("Sample text", outputResult.getSavedResults().get(0));

    Inputs.setInputs(null);
  }
}
