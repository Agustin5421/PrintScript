package linter.visitor.strategy.vardec;

import ast.expressions.ExpressionNode;
import ast.identifier.Identifier;
import ast.root.AstNode;
import ast.statements.VariableDeclaration;
import linter.visitor.report.FullReport;
import linter.visitor.strategy.LintingStrategy;
import linter.visitor.strategy.NewLinterVisitor;

public class VariableDeclarationTraversing implements LintingStrategy {
  private final LintingStrategy selfStrategy;

  public VariableDeclarationTraversing(LintingStrategy selfStrategy) {
    this.selfStrategy = selfStrategy;
  }

  @Override
  public FullReport oldApply(AstNode node, FullReport fullReport) {
    return null;
  }

  @Override
  public NewLinterVisitor apply(AstNode node, NewLinterVisitor visitor) {
    VariableDeclaration variableDeclaration = (VariableDeclaration) node;

    Identifier identifier = variableDeclaration.identifier();
    visitor = visitor.lintNode(identifier);

    ExpressionNode expression = variableDeclaration.expression();
    visitor = visitor.lintNode(expression);

    visitor = selfStrategy.apply(variableDeclaration, visitor);

    return visitor;
  }
}
