package formatter.strategy.word;

import ast.identifier.Identifier;
import ast.root.AstNode;
import formatter.FormattingEngine;
import formatter.strategy.FormattingStrategy;

public class IdentifierStrategy implements FormattingStrategy {

  @Override
  public FormattingEngine apply(AstNode node, FormattingEngine engine) {
    Identifier identifier = (Identifier) node;
    engine.write(identifier.name());
    return engine;
  }
}
