package formatter.strategy.ifelse;

import ast.root.AstNode;
import ast.statements.IfStatement;
import formatter.strategy.FormattingStrategy;
import formatter.strategy.common.CharacterStrategy;
import formatter.visitor.FormatterVisitor;
import java.util.List;

public class IfElseStrategy implements FormattingStrategy {
  private final ConditionalStatementStrategy conditionalStatementStrategy;
  // List of two white spaces for the else keyword (before and after)
  private final List<CharacterStrategy> whiteSpaces;
  // Strategy that adds indentation depending on the level of the node (getValue())
  private final BodyStrategy bodyStrategy;
  private final IndentStrategy indentStrategy;

  public IfElseStrategy(
      ConditionalStatementStrategy conditionalStatementStrategy,
      List<CharacterStrategy> whiteSpaces,
      IndentStrategy indentStrategy) {
    this.conditionalStatementStrategy = conditionalStatementStrategy;
    this.whiteSpaces = whiteSpaces;
    this.bodyStrategy = new BodyStrategy(indentStrategy);
    this.indentStrategy = indentStrategy;
  }

  @Override
  public String apply(AstNode node, FormatterVisitor visitor) {
    StringBuilder formattedCode = new StringBuilder();
    IfStatement ifStatementNode = (IfStatement) node;

    // Adding "if (condition)"
    String condition = formatCondition(ifStatementNode, node, visitor);
    formattedCode.append(condition);

    // Enter 1 more level of indentation
    FormatterVisitor newVisitor = visitor.cloneVisitor();

    // Format the whole if body
    BodyStrategy newBodyStrategy =
        bodyStrategy.newStrategy(ifStatementNode.getThenBlockStatement());
    String ifBody = formatBody(newBodyStrategy, node, visitor, newVisitor);
    formattedCode.append(ifBody);

    // Format the else block if it exists
    if (!ifStatementNode.getElseBlockStatement().isEmpty()) {
      formattedCode.append(whiteSpaces.get(0).apply(node, newVisitor));
      formattedCode.append("else");
      formattedCode.append(whiteSpaces.get(1).apply(node, newVisitor));

      newBodyStrategy = bodyStrategy.newStrategy(ifStatementNode.getElseBlockStatement());
      String elseBody = formatBody(newBodyStrategy, node, visitor, newVisitor);
      formattedCode.append(elseBody);
    }
    return formattedCode.toString();
  }

  public String formatCondition(
      IfStatement ifStatementNode, AstNode node, FormatterVisitor visitor) {
    FormatterVisitor newVisitor = (FormatterVisitor) ifStatementNode.getCondition().accept(visitor);
    String condition = newVisitor.getCurrentCode();

    // Set the condition and keyword for the already existing strategy
    ConditionalStatementStrategy newCondStrategy =
        conditionalStatementStrategy.newStrategy("if", condition);
    return newCondStrategy.apply(node, visitor);
  }

  public String formatBody(
      BodyStrategy bodyStrategy,
      AstNode node,
      FormatterVisitor previousVisitor,
      FormatterVisitor visitor) {
    StringBuilder formattedCode = new StringBuilder();
    formattedCode.append("{\n");
    formattedCode.append(bodyStrategy.apply(node, visitor));
    formattedCode.append(indentStrategy.apply(node, previousVisitor));
    formattedCode.append("}");
    return formattedCode.toString();
  }
}
