package linter.rework.visitor;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.root.AstNode;
import ast.root.AstNodeType;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.VariableDeclaration;
import ast.visitor.NodeVisitor;
import java.util.Map;
import linter.rework.report.FullReport;
import linter.rework.visitor.strategy.LintingStrategy;

public class LinterVisitorV2 implements NodeVisitor {
  private final FullReport fullReport;
  private final Map<AstNodeType, LintingStrategy> nodesStrategies;

  public LinterVisitorV2(Map<AstNodeType, LintingStrategy> nodesStrategies) {
    this(new FullReport(), nodesStrategies);
  }

  public LinterVisitorV2(FullReport fullReport, Map<AstNodeType, LintingStrategy> nodesStrategies) {
    this.fullReport = fullReport;
    this.nodesStrategies = nodesStrategies;
  }

  public FullReport getFullReport() {
    return fullReport;
  }

  public Map<AstNodeType, LintingStrategy> getNodesStrategies() {
    return nodesStrategies;
  }

  @Override
  public NodeVisitor visitCallExpression(CallExpression callExpression) {
    Identifier methodIdentifier = callExpression.methodIdentifier();
    NodeVisitor visitor = methodIdentifier.accept(this);

    for (AstNode argument : callExpression.arguments()) {
      visitor = argument.accept(visitor);
    }

    FullReport newReport = ((LinterVisitorV2) visitor).getFullReport();

    LintingStrategy strategy = getNodesStrategies().get(callExpression.getType());

    if (strategy != null) {
      newReport = strategy.apply(callExpression, newReport);
    }

    return new LinterVisitorV2(newReport, getNodesStrategies());
  }

  @Override
  public NodeVisitor visitAssignmentExpression(AssignmentExpression assignmentExpression) {
    return this;
  }

  @Override
  public NodeVisitor visitVarDec(VariableDeclaration variableDeclaration) {
    return this;
  }

  @Override
  public NodeVisitor visitNumberLiteral(NumberLiteral numberLiteral) {
    LintingStrategy strategy = getNodesStrategies().get(numberLiteral.getType());

    if (strategy != null) {
      FullReport newReport = strategy.apply(numberLiteral, getFullReport());
      return new LinterVisitorV2(newReport, getNodesStrategies());
    }

    return this;
  }

  @Override
  public NodeVisitor visitStringLiteral(StringLiteral stringLiteral) {
    LintingStrategy strategy = getNodesStrategies().get(stringLiteral.getType());

    if (strategy != null) {
      FullReport newReport = strategy.apply(stringLiteral, getFullReport());
      return new LinterVisitorV2(newReport, getNodesStrategies());
    }

    return this;
  }

  @Override
  public NodeVisitor visitIdentifier(Identifier identifier) {
    LintingStrategy strategy = getNodesStrategies().get(identifier.getType());

    if (strategy != null) {
      FullReport newReport = strategy.apply(identifier, getFullReport());
      return new LinterVisitorV2(newReport, getNodesStrategies());
    }

    return this;
  }

  @Override
  public NodeVisitor visitBinaryExpression(BinaryExpression binaryExpression) {
    return this;
  }
}