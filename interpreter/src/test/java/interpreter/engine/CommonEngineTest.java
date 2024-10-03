package interpreter.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.BooleanLiteral;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.root.AstNode;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.VariableDeclaration;
import interpreter.engine.repository.VariableIdentifier;
import interpreter.engine.repository.VariableIdentifierFactory;
import interpreter.engine.repository.VariablesRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import output.OutputListString;
import position.Position;

public abstract class CommonEngineTest {
  protected abstract InterpreterEngine getEngine();

  @Test
  public void interpretVariableDeclarationWithString() {
    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("x", position, position);
    StringLiteral stringLiteral = new StringLiteral("Hello, World!", position, position);
    VariableDeclaration variableDeclaration =
        new VariableDeclaration("let", identifier, stringLiteral, "string", position, position);

    InterpreterEngine engine = getEngine();
    InterpreterEngine result = engine.interpret(variableDeclaration);

    // Verify immutability of the engine.
    VariablesRepository originalVarRepo = engine.getVariablesRepository();
    VariableIdentifier variableIdentifier =
        VariableIdentifierFactory.createVarIdFromIdentifier(identifier);
    assertEquals(0, originalVarRepo.getNewVariables().size());
    assertNull(originalVarRepo.getNewVariable(variableIdentifier));

    // Verify that the variable was created and assigned the correct value.
    VariablesRepository variablesRepository = result.getVariablesRepository();
    assertEquals(
        stringLiteral.value(), variablesRepository.getNewVariable(variableIdentifier).value());
  }

  @Test
  public void interpretVariableDeclarationWithNumber() {
    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("x", position, position);
    NumberLiteral numberLiteral = new NumberLiteral(32, position, position);
    VariableDeclaration variableDeclaration =
        new VariableDeclaration("let", identifier, numberLiteral, "string", position, position);

    InterpreterEngine engine = getEngine();
    engine = engine.interpret(variableDeclaration);

    // Verify that the variable was created and assigned the correct value.
    VariableIdentifier variableIdentifier =
        VariableIdentifierFactory.createVarIdFromIdentifier(identifier);
    VariablesRepository variablesRepository = engine.getVariablesRepository();
    assertEquals(
        numberLiteral.value(), variablesRepository.getNewVariable(variableIdentifier).value());
  }

  @Test
  public void interpretPrintlnCallExpressionWithString() {
    Position position = new Position(0, 0);
    StringLiteral stringLiteral = new StringLiteral("Hello, World!", position, position);
    Identifier identifier = new Identifier("println", position, position);
    CallExpression callExpression = new CallExpression(identifier, List.of(stringLiteral));

    InterpreterEngine visitor = getEngine();
    visitor.interpret(callExpression);

    OutputListString outputResult = (OutputListString) visitor.getOutputResult();
    assertEquals("Hello, World!", outputResult.getSavedResults().get(0));
  }

  @Test
  public void interpretPrintlnCallExpressionWithNumber() {
    Position position = new Position(0, 0);
    NumberLiteral numberLiteral = new NumberLiteral(32, position, position);
    Identifier identifier = new Identifier("println", position, position);
    CallExpression callExpression = new CallExpression(identifier, List.of(numberLiteral));

    InterpreterEngine visitor = getEngine();
    visitor.interpret(callExpression);

    OutputListString outputResult = (OutputListString) visitor.getOutputResult();
    assertEquals("32", outputResult.getSavedResults().get(0));
  }

  @Test
  public void interpretPrintlnCallExpressionWithVariable() {
    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("x", position, position);
    StringLiteral stringLiteral = new StringLiteral("Hello, World!", position, position);
    VariableDeclaration variableDeclaration =
        new VariableDeclaration("let", identifier, stringLiteral, "string", position, position);

    Identifier methodName = new Identifier("println", position, position);
    CallExpression callExpression = new CallExpression(methodName, List.of(identifier));

    InterpreterEngine engine = getEngine();
    engine = engine.interpret(variableDeclaration);
    engine.interpret(callExpression);

    OutputListString outputResult = (OutputListString) engine.getOutputResult();
    assertEquals("Hello, World!", outputResult.getSavedResults().get(0));
  }

  @Test
  public void interpretPrintlnCallExpressionSeveralTimes() {
    Position position = new Position(0, 0);
    StringLiteral stringLiteral = new StringLiteral("Hello, World!", position, position);
    Identifier identifier = new Identifier("println", position, position);
    CallExpression callExpression = new CallExpression(identifier, List.of(stringLiteral));

    InterpreterEngine visitor = getEngine();
    visitor.interpret(callExpression);
    visitor.interpret(callExpression);
    visitor.interpret(callExpression);
    visitor.interpret(callExpression);
    visitor.interpret(callExpression);

    OutputListString outputResult = (OutputListString) visitor.getOutputResult();
    for (int i = 0; i < 4; i++) {
      assertEquals("Hello, World!", outputResult.getSavedResults().get(i));
    }
  }

  @Test
  public void failWithNonStatementNodes() {
    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("x", position, position);
    NumberLiteral numberLiteral = new NumberLiteral(32, position, position);
    StringLiteral stringLiteral = new StringLiteral("Hello, World!", position, position);
    BooleanLiteral booleanLiteral = new BooleanLiteral(true, position, position);
    List<AstNode> nodes = List.of(identifier, numberLiteral, stringLiteral, booleanLiteral);

    InterpreterEngine visitor = getEngine();

    for (AstNode node : nodes) {
      assertThrows(Exception.class, () -> visitor.interpret(node));
    }
  }

  @Test
  public void interpretAssignmentExpression() {
    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("x", position, position);
    StringLiteral stringLiteral = new StringLiteral("Hello, World!", position, position);
    VariableDeclaration variableDeclaration =
        new VariableDeclaration("let", identifier, stringLiteral, "string", position, position);

    StringLiteral newStringLiteral = new StringLiteral("Goodbye, World!", position, position);
    AssignmentExpression assignmentExpression =
        new AssignmentExpression(identifier, newStringLiteral, "=");

    InterpreterEngine engine = getEngine();
    engine = engine.interpret(variableDeclaration);
    engine = engine.interpret(assignmentExpression);

    VariableIdentifier variableIdentifier =
        VariableIdentifierFactory.createVarIdFromIdentifier(identifier);
    VariablesRepository variablesRepository = engine.getVariablesRepository();
    assertEquals(
        newStringLiteral.value(), variablesRepository.getNewVariable(variableIdentifier).value());
  }

  @Test
  public void failAssignmentExpressionWithDifferentType() {
    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("x", position, position);
    StringLiteral stringLiteral = new StringLiteral("Hello, World!", position, position);
    VariableDeclaration variableDeclaration =
        new VariableDeclaration("let", identifier, stringLiteral, "string", position, position);

    NumberLiteral numberLiteral = new NumberLiteral(32, position, position);
    AssignmentExpression assignmentExpression =
        new AssignmentExpression(identifier, numberLiteral, "=");

    InterpreterEngine engine = getEngine();
    engine = engine.interpret(variableDeclaration);
    InterpreterEngine finalVisitor = engine;
    assertThrows(Exception.class, () -> finalVisitor.interpret(assignmentExpression));
  }

  @Test
  public void interpretBinaryExpressionWithNumber() {
    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("x", position, position);
    NumberLiteral numberLiteral = new NumberLiteral(32, position, position);
    BinaryExpression binaryExpression =
        new BinaryExpression(numberLiteral, numberLiteral, "+", position, position);
    VariableDeclaration variableDeclaration =
        new VariableDeclaration("let", identifier, binaryExpression, "number", position, position);

    InterpreterEngine engine = getEngine();
    engine = engine.interpret(variableDeclaration);

    VariableIdentifier variableIdentifier =
        VariableIdentifierFactory.createVarIdFromIdentifier(identifier);
    VariablesRepository variablesRepository = engine.getVariablesRepository();
    assertEquals(64, variablesRepository.getNewVariable(variableIdentifier).value());
  }

  @Test
  public void interpretBinaryExpressionWithString() {
    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("x", position, position);
    StringLiteral stringLiteral1 = new StringLiteral("Hello,", position, position);
    StringLiteral stringLiteral2 = new StringLiteral(" World!", position, position);
    BinaryExpression binaryExpression =
        new BinaryExpression(stringLiteral1, stringLiteral2, "+", position, position);
    VariableDeclaration variableDeclaration =
        new VariableDeclaration("let", identifier, binaryExpression, "string", position, position);

    InterpreterEngine engine = getEngine();
    engine = engine.interpret(variableDeclaration);

    VariableIdentifier variableIdentifier =
        VariableIdentifierFactory.createVarIdFromIdentifier(identifier);
    VariablesRepository variablesRepository = engine.getVariablesRepository();
    assertEquals("Hello, World!", variablesRepository.getNewVariable(variableIdentifier).value());
  }

  @Test
  public void interpretBinaryExpressionWithStringNumber() {
    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("x", position, position);
    StringLiteral stringLiteral = new StringLiteral("Hello, ", position, position);
    NumberLiteral numberLiteral = new NumberLiteral(32, position, position);
    BinaryExpression binaryExpression =
        new BinaryExpression(stringLiteral, numberLiteral, "+", position, position);
    VariableDeclaration variableDeclaration =
        new VariableDeclaration("let", identifier, binaryExpression, "string", position, position);

    InterpreterEngine engine = getEngine();
    engine = engine.interpret(variableDeclaration);

    VariableIdentifier variableIdentifier =
        VariableIdentifierFactory.createVarIdFromIdentifier(identifier);
    VariablesRepository variablesRepository = engine.getVariablesRepository();
    assertEquals("Hello, 32", variablesRepository.getNewVariable(variableIdentifier).value());
  }

  @Test
  public void interpretBinaryExpressionWithStringIdentifier() {
    Position position = new Position(0, 0);
    Identifier identifierX = new Identifier("x", position, position);
    StringLiteral stringLiteral = new StringLiteral("Hello, ", position, position);
    VariableDeclaration variableDeclaration =
        new VariableDeclaration("let", identifierX, stringLiteral, "string", position, position);

    BinaryExpression binaryExpression =
        new BinaryExpression(identifierX, stringLiteral, "+", position, position);
    Identifier identifierY = new Identifier("y", position, position);
    VariableDeclaration variableDeclaration2 =
        new VariableDeclaration("let", identifierY, binaryExpression, "string", position, position);

    InterpreterEngine engine = getEngine();
    engine = engine.interpret(variableDeclaration);
    engine = engine.interpret(variableDeclaration2);

    VariableIdentifier variableIdentifier =
        VariableIdentifierFactory.createVarIdFromIdentifier(identifierY);
    VariablesRepository variablesRepository = engine.getVariablesRepository();
    assertEquals("Hello, Hello, ", variablesRepository.getNewVariable(variableIdentifier).value());
  }
}
