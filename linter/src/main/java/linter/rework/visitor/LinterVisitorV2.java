package linter.rework.visitor;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.VariableDeclaration;
import ast.visitor.NodeVisitor;
import java.util.Map;
import linter.rework.report.FullReport;
import linter.rework.visitor.strategy.LintingStrategy;

public class LinterVisitorV2 implements NodeVisitor {
  private final FullReport fullReport;
  private final Map<Class<?>, LintingStrategy<?>> nodesStrategies;

  public LinterVisitorV2(Map<Class<?>, LintingStrategy<?>> nodesStrategies) {
    this(new FullReport(), nodesStrategies);
  }

  public LinterVisitorV2(FullReport fullReport, Map<Class<?>, LintingStrategy<?>> nodesStrategies) {
    this.fullReport = fullReport;
    this.nodesStrategies = nodesStrategies;
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
