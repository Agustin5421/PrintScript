package linter.rework.visitor;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.VariableDeclaration;
import ast.visitor.NodeVisitor;
import linter.rework.report.FullReport;

public class LinterVisitorV2 implements NodeVisitor {
  private final FullReport fullReport;

  public LinterVisitorV2() {
    this(new FullReport());
  }

  public LinterVisitorV2(FullReport fullReport) {
    this.fullReport = fullReport;
  }

  @Override
  public NodeVisitor visitCallExpression(CallExpression callExpression) {
    return null;
  }

  @Override
  public NodeVisitor visitAssignmentExpression(AssignmentExpression assignmentExpression) {
    return null;
  }

  @Override
  public NodeVisitor visitVarDec(VariableDeclaration variableDeclaration) {
    return null;
  }

  @Override
  public NodeVisitor visitNumberLiteral(NumberLiteral numberLiteral) {
    return null;
  }

  @Override
  public NodeVisitor visitStringLiteral(StringLiteral stringLiteral) {
    return null;
  }

  @Override
  public NodeVisitor visitIdentifier(Identifier identifier) {
    return null;
  }

  @Override
  public NodeVisitor visitBinaryExpression(BinaryExpression binaryExpression) {
    return null;
  }
}
