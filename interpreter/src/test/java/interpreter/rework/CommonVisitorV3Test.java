package interpreter.rework;

import static org.junit.jupiter.api.Assertions.*;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.BooleanLiteral;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.root.AstNode;
import ast.statements.CallExpression;
import ast.statements.VariableDeclaration;
import interpreter.visitor.InterpreterVisitorV3;
import interpreter.visitor.repository.VariableIdentifier;
import interpreter.visitor.repository.VariableIdentifierFactory;
import interpreter.visitor.repository.VariablesRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import output.OutputListString;
import token.Position;

public abstract class CommonVisitorV3Test {
  protected abstract InterpreterVisitorV3 getVisitor();

  @Test
  public void interpretVariableDeclarationWithString() {
    Position position = new Position(0, 0);
    Identifier identifier = new Identifier("x", position, position);
    StringLiteral stringLiteral = new StringLiteral("Hello, World!", position, position);
    VariableDeclaration variableDeclaration =
        new VariableDeclaration("let", identifier, stringLiteral, "string", position, position);

    InterpreterVisitorV3 visitor = getVisitor();
    InterpreterVisitorV3 result = (InterpreterVisitorV3) visitor.visit(variableDeclaration);

    // Verify immutability of the visitor.
    VariablesRepository originalVarRepo = visitor.getVariablesRepository();
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

    InterpreterVisitorV3 visitor = getVisitor();
    visitor = (InterpreterVisitorV3) visitor.visit(variableDeclaration);

    // Verify that the variable was created and assigned the correct value.
    VariableIdentifier variableIdentifier =
        VariableIdentifierFactory.createVarIdFromIdentifier(identifier);
    VariablesRepository variablesRepository = visitor.getVariablesRepository();
    assertEquals(
        numberLiteral.value(), variablesRepository.getNewVariable(variableIdentifier).value());
  }

  @Test
  public void interpretPrintlnCallExpressionWithString() {
    Position position = new Position(0, 0);
    StringLiteral stringLiteral = new StringLiteral("Hello, World!", position, position);
    Identifier identifier = new Identifier("println", position, position);
    CallExpression callExpression = new CallExpression(identifier, List.of(stringLiteral));

    InterpreterVisitorV3 visitor = getVisitor();
    visitor.visit(callExpression);

    OutputListString outputResult = (OutputListString) visitor.getOutputResult();
    assertEquals("Hello, World!", outputResult.getSavedResults().get(0));
  }

  @Test
  public void interpretPrintlnCallExpressionWithNumber() {
    Position position = new Position(0, 0);
    NumberLiteral numberLiteral = new NumberLiteral(32, position, position);
    Identifier identifier = new Identifier("println", position, position);
    CallExpression callExpression = new CallExpression(identifier, List.of(numberLiteral));

    InterpreterVisitorV3 visitor = getVisitor();
    visitor.visit(callExpression);

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

    InterpreterVisitorV3 visitor = getVisitor();
    visitor = (InterpreterVisitorV3) visitor.visit(variableDeclaration);
    visitor.visit(callExpression);

    OutputListString outputResult = (OutputListString) visitor.getOutputResult();
    assertEquals("Hello, World!", outputResult.getSavedResults().get(0));
  }

  @Test
  public void interpretPrintlnCallExpressionSeveralTimes() {
    Position position = new Position(0, 0);
    StringLiteral stringLiteral = new StringLiteral("Hello, World!", position, position);
    Identifier identifier = new Identifier("println", position, position);
    CallExpression callExpression = new CallExpression(identifier, List.of(stringLiteral));

    InterpreterVisitorV3 visitor = getVisitor();
    visitor.visit(callExpression);
    visitor.visit(callExpression);
    visitor.visit(callExpression);
    visitor.visit(callExpression);
    visitor.visit(callExpression);

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

    InterpreterVisitorV3 visitor = getVisitor();

    for (AstNode node : nodes) {
      assertThrows(Exception.class, () -> visitor.visit(node));
    }
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

    InterpreterVisitorV3 visitor = getVisitor();
    visitor = (InterpreterVisitorV3) visitor.visit(variableDeclaration);

    VariableIdentifier variableIdentifier =
        VariableIdentifierFactory.createVarIdFromIdentifier(identifier);
    VariablesRepository variablesRepository = visitor.getVariablesRepository();
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

    InterpreterVisitorV3 visitor = getVisitor();
    visitor = (InterpreterVisitorV3) visitor.visit(variableDeclaration);

    VariableIdentifier variableIdentifier =
        VariableIdentifierFactory.createVarIdFromIdentifier(identifier);
    VariablesRepository variablesRepository = visitor.getVariablesRepository();
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

    InterpreterVisitorV3 visitor = getVisitor();
    visitor = (InterpreterVisitorV3) visitor.visit(variableDeclaration);

    VariableIdentifier variableIdentifier =
        VariableIdentifierFactory.createVarIdFromIdentifier(identifier);
    VariablesRepository variablesRepository = visitor.getVariablesRepository();
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

    InterpreterVisitorV3 visitor = getVisitor();
    visitor = (InterpreterVisitorV3) visitor.visit(variableDeclaration);
    visitor = (InterpreterVisitorV3) visitor.visit(variableDeclaration2);

    VariableIdentifier variableIdentifier =
        VariableIdentifierFactory.createVarIdFromIdentifier(identifierY);
    VariablesRepository variablesRepository = visitor.getVariablesRepository();
    assertEquals("Hello, Hello, ", variablesRepository.getNewVariable(variableIdentifier).value());
  }
}
