package formatter.strategy.word.literal;

import ast.literal.BooleanLiteral;
import ast.root.AstNode;
import formatter.FormattingEngine;
import formatter.strategy.FormattingStrategy;

public class BooleanStrategy implements FormattingStrategy {
  @Override
  public FormattingEngine apply(AstNode node, FormattingEngine engine) {
    BooleanLiteral booleanLiteral = (BooleanLiteral) node;
    engine.write(booleanLiteral.value().toString());
    return engine;
  }
}
