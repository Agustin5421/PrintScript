package formatter.strategy.ifelse;

import ast.root.AstNode;
import ast.statements.StatementNode;
import formatter.strategy.FormattingStrategy;
import formatter.visitor.FormatterVisitor;
import java.util.List;

public class BodyStrategy implements FormattingStrategy {
  private final List<StatementNode> body;
  private final IndentStrategy indentStrategy;

  public BodyStrategy(List<StatementNode> body, IndentStrategy indentStrategy) {
    this.body = body;
    this.indentStrategy = indentStrategy;
  }

  @Override
  public String apply(AstNode node, FormatterVisitor visitor) {
    StringBuilder formattedCode = new StringBuilder();
    for (StatementNode statement : body) {
      formattedCode.append(indentStrategy.apply(node, visitor));
      formattedCode.append(((FormatterVisitor) statement.accept(visitor)).getCurrentCode());
    }
    return formattedCode.toString();
  }
}
