package formatter.strategy.vardec;

import ast.root.AstNode;
import ast.statements.VariableDeclaration;
import formatter.strategy.FormattingStrategy;
import formatter.visitor.FormatterVisitor;

public class GetTypeStrategy implements FormattingStrategy {
  @Override
  public String apply(AstNode node, FormatterVisitor visitor) {
    return ((VariableDeclaration) node).varType();
  }
}
