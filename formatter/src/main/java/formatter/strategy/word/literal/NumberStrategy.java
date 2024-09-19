package formatter.strategy.word.literal;

import ast.literal.NumberLiteral;
import ast.root.AstNode;
import formatter.FormattingEngine;
import formatter.strategy.FormattingStrategy;

public class NumberStrategy implements FormattingStrategy {
  @Override
  public FormattingEngine apply(AstNode node, FormattingEngine engine) {
    NumberLiteral numberLiteral = (NumberLiteral) node;
    engine.write(numberLiteral.value().toString());
    return engine;
  }
}
