package interpreter.visitor;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.BooleanLiteral;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.IfStatement;
import ast.statements.VariableDeclaration;
import ast.visitor.NodeVisitor;

public class InterpreterVisitorV2 implements NodeVisitor {

  private final InterpreterVisitorV1 interpreterVisitorV1;

  public InterpreterVisitorV2(InterpreterVisitorV1 interpreterVisitorV1) {
    this.interpreterVisitorV1 = interpreterVisitorV1;
  }

  @Override
  public NodeVisitor visitIfStatement(IfStatement ifStatement) {
    // Implementación específica para IfStatement
    return this;
  }

  @Override
  public NodeVisitor visitBooleanLiteral(BooleanLiteral booleanLiteral) {
    // Implementación específica para BooleanLiteral
    return this;
  }

  @Override
  public NodeVisitor visitCallExpression(CallExpression callExpression) {
    return interpreterVisitorV1.visitCallExpression(callExpression);
  }

  @Override
  public NodeVisitor visitAssignmentExpression(AssignmentExpression assignmentExpression) {
    return interpreterVisitorV1.visitAssignmentExpression(assignmentExpression);
  }

  @Override
  public NodeVisitor visitVarDec(VariableDeclaration variableDeclaration) {
    return interpreterVisitorV1.visitVarDec(variableDeclaration);
  }

  @Override
  public NodeVisitor visitNumberLiteral(NumberLiteral numberLiteral) {
    return interpreterVisitorV1.visitNumberLiteral(numberLiteral);
  }

  @Override
  public NodeVisitor visitStringLiteral(StringLiteral stringLiteral) {
    return interpreterVisitorV1.visitStringLiteral(stringLiteral);
  }

  @Override
  public NodeVisitor visitIdentifier(Identifier identifier) {
    return interpreterVisitorV1.visitIdentifier(identifier);
  }

  @Override
  public NodeVisitor visitBinaryExpression(BinaryExpression binaryExpression) {
    return interpreterVisitorV1.visitBinaryExpression(binaryExpression);
  }
}
