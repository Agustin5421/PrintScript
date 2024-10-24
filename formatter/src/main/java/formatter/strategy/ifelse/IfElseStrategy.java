package formatter.strategy.ifelse;

import ast.root.AstNode;
import ast.statements.IfStatement;
import formatter.FormattingEngine;
import formatter.strategy.FormattingStrategy;
import formatter.strategy.common.CharacterStrategy;
import java.util.List;

public class IfElseStrategy implements FormattingStrategy {
  private final ConditionalStatementStrategy conditionalStatementStrategy;
  // List of two white spaces for the else keyword (before and after)
  private final List<CharacterStrategy> whiteSpaces;

  // Strategy that adds indentation depending on the level of the node (getValue())

  public IfElseStrategy(
      ConditionalStatementStrategy conditionalStatementStrategy,
      List<CharacterStrategy> whiteSpaces) {
    this.conditionalStatementStrategy = conditionalStatementStrategy;
    this.whiteSpaces = whiteSpaces;
  }

  @Override
  public FormattingEngine apply(AstNode node, FormattingEngine engine) {
    IfStatement ifStatementNode = (IfStatement) node;

    // Adding "if (condition)"
    ConditionalStatementStrategy newCondStrategy =
        conditionalStatementStrategy.newStrategy("if", ifStatementNode.getCondition());
    newCondStrategy.apply(node, engine);

    // Enter 1 more level of indentation
    FormattingEngine newEngine = engine.changeContext();

    // Format the whole if body
    BodyStrategy bodyStrategy = new BodyStrategy(ifStatementNode.getThenBlockStatement());
    formatBody(bodyStrategy, node, engine, newEngine);

    // Format the else block if it exists
    if (!ifStatementNode.getElseBlockStatement().isEmpty()) {
      whiteSpaces.get(0).apply(node, newEngine);
      engine.write("else");
      whiteSpaces.get(1).apply(node, newEngine);

      bodyStrategy = new BodyStrategy(ifStatementNode.getElseBlockStatement());
      formatBody(bodyStrategy, node, engine, newEngine);
    }

    engine.write("\n");

    return engine;
  }

  public void formatBody(
      BodyStrategy bodyStrategy,
      AstNode node,
      FormattingEngine engine,
      FormattingEngine newEngine) {
    newEngine.write("{\n");
    bodyStrategy.apply(node, newEngine);
    engine.addContext();
    newEngine.write("}");
  }
}
