package formatter.strategy.ifelse;

import ast.root.AstNode;
import ast.statements.StatementNode;
import formatter.FormattingEngine;
import formatter.strategy.FormattingStrategy;
import java.util.List;

public class BodyStrategy implements FormattingStrategy {
  private final List<StatementNode> body;

  public BodyStrategy(List<StatementNode> body) {
    this.body = body;
  }

  @Override
  public FormattingEngine apply(AstNode node, FormattingEngine engine) {
    for (StatementNode statement : body) {
      // Write indentation
      engine.addContext();
      // Write statement
      engine.format(statement);
    }
    return engine;
  }
}
