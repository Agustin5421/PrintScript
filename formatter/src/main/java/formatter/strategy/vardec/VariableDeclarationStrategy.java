package formatter.strategy.vardec;

import ast.root.AstNode;
import ast.statements.VariableDeclaration;
import exceptions.UnsupportedExpressionException;
import formatter.strategy.FormattingStrategy;
import formatter.visitor.FormatterVisitor;
import java.util.List;

public class VariableDeclarationStrategy implements FormattingStrategy {
  // Strategies should be having (or not) the first space, : and the
  // second space, then the nodeType of the node
  // then the assignment strategy
  private final List<FormattingStrategy> strategies;
  private final List<String> keyWords;

  public VariableDeclarationStrategy(List<FormattingStrategy> strategies, List<String> keyWords) {
    this.strategies = strategies;
    this.keyWords = keyWords;
  }

  @Override
  public String apply(AstNode node, FormatterVisitor visitor) {
    VariableDeclaration varDecNode = (VariableDeclaration) node;
    StringBuilder formattedCode = new StringBuilder();
    String kind = varDecNode.kind();
    if (!keyWords.contains(kind)) {
      throw new UnsupportedExpressionException(kind + " is not supported in this version");
    }
    formattedCode.append(kind).append(" ");
    // Adding the identifier
    FormatterVisitor visit = (FormatterVisitor) varDecNode.identifier().accept(visitor);
    formattedCode.append(visit.getCurrentCode());
    // Adding the whitespaces strategies
    for (FormattingStrategy strategy : strategies) {
      formattedCode.append(strategy.apply(varDecNode, visitor));
    }
    // Formatting the expression
    formattedCode.append(
        ((FormatterVisitor) varDecNode.expression().accept(visitor)).getCurrentCode());
    formattedCode.append(";");
    return formattedCode.toString();
  }
}
