package formatter.strategy.ifelse;

import ast.root.AstNode;
import ast.statements.IfStatement;
import formatter.strategy.FormattingStrategy;
import formatter.strategy.common.CharacterStrategy;
import formatter.visitor.FormatterVisitor;
import java.util.List;

public class IfElseStrategy implements FormattingStrategy {
  private final ConditionalStatementStrategy conditionalStatementStrategy;
  private final List<CharacterStrategy> whiteSpaces;
  private final IndentStrategy indentStrategy;

  public IfElseStrategy(
      FormattingStrategy conditionalStatementStrategy,
      List<CharacterStrategy> whiteSpaces,
      IndentStrategy indentStrategy) {
    this.conditionalStatementStrategy = (ConditionalStatementStrategy) conditionalStatementStrategy;
    this.whiteSpaces = whiteSpaces;
    this.indentStrategy = indentStrategy;
  }

  @Override
  public String apply(AstNode node, FormatterVisitor visitor) {
    StringBuilder formattedCode = new StringBuilder();
    IfStatement ifStatementNode = (IfStatement) node;

    String condition = formatCondition(ifStatementNode, node, visitor);
    formattedCode.append(condition);

    // Enter 1 more level of indentation
    FormatterVisitor newVisitor = visitor.cloneVisitor();

    // Format the whole if body
    BodyStrategy bodyStrategy =
        new BodyStrategy(ifStatementNode.getThenBlockStatement(), indentStrategy);
    String ifBody = formatBody(bodyStrategy, node, visitor, newVisitor, indentStrategy);
    formattedCode.append(ifBody);

    // Format the else block if it exists
    if (!ifStatementNode.getElseBlockStatement().isEmpty()) {
      formattedCode.append(whiteSpaces.get(0).apply(node, newVisitor));
      formattedCode.append("else");
      formattedCode.append(whiteSpaces.get(1).apply(node, newVisitor));

      bodyStrategy = new BodyStrategy(ifStatementNode.getElseBlockStatement(), indentStrategy);
      String elseBody = formatBody(bodyStrategy, node, visitor, newVisitor, indentStrategy);
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
      FormatterVisitor visitor,
      IndentStrategy indentStrategy) {
    StringBuilder formattedCode = new StringBuilder();
    formattedCode.append("{\n");
    formattedCode.append(bodyStrategy.apply(node, visitor));
    formattedCode.append(indentStrategy.apply(node, previousVisitor));
    formattedCode.append("}");
    return formattedCode.toString();
  }
}
