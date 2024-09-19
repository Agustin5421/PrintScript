package formatter.strategy.word.literal;

import ast.literal.StringLiteral;
import ast.root.AstNode;
import formatter.FormattingEngine;
import formatter.strategy.FormattingStrategy;

public class StringStrategy implements FormattingStrategy {
  @Override
  public FormattingEngine apply(AstNode node, FormattingEngine engine) {
    StringLiteral stringLiteral = (StringLiteral) node;
    engine.write("\"");
    engine.write(stringLiteral.value());
    engine.write("\"");
    return engine;
  }
}
