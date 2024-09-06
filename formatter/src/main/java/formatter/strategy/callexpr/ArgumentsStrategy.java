package formatter.strategy.callexpr;

import ast.root.AstNode;
import ast.statements.CallExpression;
import formatter.FormatterVisitor;
import formatter.strategy.FormattingStrategy;

public class ArgumentsStrategy implements FormattingStrategy {
  private final FormattingStrategy whiteSpace;

  public ArgumentsStrategy(FormattingStrategy whiteSpace) {
    this.whiteSpace = whiteSpace;
  }

  @Override
  public String apply(AstNode node, FormatterVisitor visitor) {
    CallExpression callExpression = (CallExpression) node;
    StringBuilder formattedCode = new StringBuilder();
    int argumentCount = callExpression.arguments().size();

    for (int i = 0; i < argumentCount; i++) {
      AstNode argument = callExpression.arguments().get(i);
      formattedCode.append(((FormatterVisitor) argument.accept(visitor)).getCurrentCode());
      if (i < argumentCount - 1) {
        formattedCode.append(",");
        formattedCode.append(whiteSpace.apply(node, visitor));
      }
    }
    return formattedCode.toString();
  }
}
