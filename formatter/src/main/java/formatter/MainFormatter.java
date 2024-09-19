package formatter;

import ast.root.AstNode;

public class MainFormatter {
  private final FormattingEngine engine;

  public MainFormatter(FormattingEngine engine) {
    this.engine = engine;
  }

  public MainFormatter formatNext(AstNode node) {
    FormattingEngine visitor = this.engine.format(node);
    return new MainFormatter(visitor);
  }
}
