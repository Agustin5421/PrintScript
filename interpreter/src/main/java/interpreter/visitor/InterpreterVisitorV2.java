package interpreter.visitor;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.BooleanLiteral;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.root.AstNode;
import ast.root.AstNodeType;
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
  public NodeVisitor visit(AstNode node) {
    AstNodeType nodeType = node.getNodeType();
    if (nodeType == AstNodeType.IF_STATEMENT) {
      return visitIfStatement((IfStatement) node);
    } else if (nodeType == AstNodeType.BOOLEAN_LITERAL) {
      return visitBooleanLiteral((BooleanLiteral) node);
    } else {
      return interpreterVisitorV1.visit(node);
    }
  }

  private NodeVisitor visitIfStatement(IfStatement ifStatement) {
    // Implementación específica para IfStatement
    return this;
  }

  private NodeVisitor visitBooleanLiteral(BooleanLiteral booleanLiteral) {
    // Implementación específica para BooleanLiteral
    return this;
  }

  @Override
  public NodeVisitor visitCallExpression(CallExpression callExpression) {
    return interpreterVisitorV1.visit(callExpression);
  }

  @Override
  public NodeVisitor visitAssignmentExpression(AssignmentExpression assignmentExpression) {
    return interpreterVisitorV1.visit(assignmentExpression);
  }

  @Override
  public NodeVisitor visitVarDec(VariableDeclaration variableDeclaration) {
    return interpreterVisitorV1.visit(variableDeclaration);
  }

  @Override
  public NodeVisitor visitNumberLiteral(NumberLiteral numberLiteral) {
    return interpreterVisitorV1.visit(numberLiteral);
  }

  @Override
  public NodeVisitor visitStringLiteral(StringLiteral stringLiteral) {
    return interpreterVisitorV1.visit(stringLiteral);
  }

  @Override
  public NodeVisitor visitIdentifier(Identifier identifier) {
    return interpreterVisitorV1.visit(identifier);
  }

  @Override
  public NodeVisitor visitBinaryExpression(BinaryExpression binaryExpression) {
    return interpreterVisitorV1.visit(binaryExpression);
  }
}
