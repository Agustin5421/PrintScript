package formatter.strategy.vardec;

import ast.root.AstNode;
import ast.statements.VariableDeclaration;
import formatter.FormattingEngine;
import formatter.strategy.FormattingStrategy;

public class GetTypeStrategy implements FormattingStrategy {
  @Override
  public FormattingEngine apply(AstNode node, FormattingEngine engine) {
    VariableDeclaration varDecNode = (VariableDeclaration) node;
    engine.write(varDecNode.varType());
    return engine;
  }
}
