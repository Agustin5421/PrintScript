package linter.visitor;

import ast.expressions.BinaryExpression;
import ast.expressions.Expression;
import ast.identifier.Identifier;
import ast.literal.BooleanLiteral;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.root.AstNodeType;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.IfStatement;
import ast.statements.VariableDeclaration;
import ast.visitor.NodeVisitor;
import java.util.Map;
import linter.visitor.report.FullReport;
import linter.visitor.strategy.LintingStrategy;

public class LinterVisitorV2 implements LinterVisitor {
  private final FullReport fullReport;
  private final Map<AstNodeType, LintingStrategy> nodesStrategies;
  private final LinterVisitor visitorV1;

  public LinterVisitorV2(
      Map<AstNodeType, LintingStrategy> nodesStrategies, LinterVisitor visitorV1) {
    this(new FullReport(), nodesStrategies, visitorV1);
  }

  public LinterVisitorV2(
      FullReport fullReport,
      Map<AstNodeType, LintingStrategy> nodesStrategies,
      LinterVisitor visitorV1) {
    this.fullReport = fullReport;
    this.nodesStrategies = nodesStrategies;
    this.visitorV1 = visitorV1;
  }

  public FullReport getFullReport() {
    return fullReport;
  }

  public Map<AstNodeType, LintingStrategy> getNodesStrategies() {
    return nodesStrategies;
  }

  public LinterVisitor getVisitorV1() {
    return visitorV1;
  }

  @Override
  public NodeVisitor visitIfStatement(IfStatement ifStatement) {
    Expression condition = ifStatement.getCondition();
    NodeVisitor visitor = condition.accept(this);

    for (var statement : ifStatement.getThenBlockStatement()) {
      visitor = statement.accept(visitor);
    }

    for (var statement : ifStatement.getElseBlockStatement()) {
      visitor = statement.accept(visitor);
    }

    FullReport newReport = ((LinterVisitor) visitor).getFullReport();
    LintingStrategy strategy = getNodesStrategies().get(ifStatement.getNodeType());
    if (strategy != null) {
      newReport = strategy.apply(ifStatement, newReport);
    }

    return new LinterVisitorV2(newReport, getNodesStrategies(), getVisitorV1());
  }

  @Override
  public NodeVisitor visitBooleanLiteral(BooleanLiteral booleanLiteral) {
    LintingStrategy strategy = getNodesStrategies().get(booleanLiteral.getNodeType());

    if (strategy != null) {
      FullReport newReport = strategy.apply(booleanLiteral, getFullReport());
      return new LinterVisitorV2(newReport, getNodesStrategies(), getVisitorV1());
    }

    return this;
  }

  @Override
  public NodeVisitor visitCallExpression(CallExpression callExpression) {
    LinterVisitor visitor = (LinterVisitor) callExpression.accept(visitorV1);
    FullReport newReport = visitor.getFullReport();

    return new LinterVisitorV2(newReport, getNodesStrategies(), getVisitorV1());
  }

  @Override
  public NodeVisitor visitAssignmentExpression(AssignmentExpression assignmentExpression) {
    LinterVisitor visitor = (LinterVisitor) assignmentExpression.accept(visitorV1);
    FullReport newReport = visitor.getFullReport();

    return new LinterVisitorV2(newReport, getNodesStrategies(), getVisitorV1());
  }

  @Override
  public NodeVisitor visitVarDec(VariableDeclaration variableDeclaration) {
    LinterVisitor visitor = (LinterVisitor) variableDeclaration.accept(visitorV1);
    FullReport newReport = visitor.getFullReport();

    return new LinterVisitorV2(newReport, getNodesStrategies(), getVisitorV1());
  }

  @Override
  public NodeVisitor visitNumberLiteral(NumberLiteral numberLiteral) {
    LinterVisitor visitor = (LinterVisitor) numberLiteral.accept(visitorV1);
    FullReport newReport = visitor.getFullReport();

    return new LinterVisitorV2(newReport, getNodesStrategies(), getVisitorV1());
  }

  @Override
  public NodeVisitor visitStringLiteral(StringLiteral stringLiteral) {
    LinterVisitor visitor = (LinterVisitor) stringLiteral.accept(visitorV1);
    FullReport newReport = visitor.getFullReport();

    return new LinterVisitorV2(newReport, getNodesStrategies(), getVisitorV1());
  }

  @Override
  public NodeVisitor visitIdentifier(Identifier identifier) {
    LinterVisitor visitor = (LinterVisitor) identifier.accept(visitorV1);
    FullReport newReport = visitor.getFullReport();

    return new LinterVisitorV2(newReport, getNodesStrategies(), getVisitorV1());
  }

  @Override
  public NodeVisitor visitBinaryExpression(BinaryExpression binaryExpression) {
    LinterVisitor visitor = (LinterVisitor) binaryExpression.accept(visitorV1);
    FullReport newReport = visitor.getFullReport();

    return new LinterVisitorV2(newReport, getNodesStrategies(), getVisitorV1());
  }
}
