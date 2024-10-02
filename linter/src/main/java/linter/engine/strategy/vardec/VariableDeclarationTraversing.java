package linter.engine.strategy.vardec;

import ast.expressions.ExpressionNode;
import ast.identifier.Identifier;
import ast.root.AstNode;
import ast.statements.VariableDeclaration;
import linter.engine.LinterEngine;
import linter.engine.strategy.LintingStrategy;

public class VariableDeclarationTraversing implements LintingStrategy {
  private final LintingStrategy selfStrategy;

  public VariableDeclarationTraversing(LintingStrategy selfStrategy) {
    this.selfStrategy = selfStrategy;
  }

  @Override
  public LinterEngine apply(AstNode node, LinterEngine engine) {
    VariableDeclaration variableDeclaration = (VariableDeclaration) node;

    Identifier identifier = variableDeclaration.identifier();
    engine = engine.lintNode(identifier);

    ExpressionNode expression = variableDeclaration.expression();
    engine = engine.lintNode(expression);

    engine = selfStrategy.apply(variableDeclaration, engine);

    return engine;
  }
}
