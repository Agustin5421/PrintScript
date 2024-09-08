package formatter.strategy.callexpr;

import ast.root.AstNode;
import ast.statements.CallExpression;
import formatter.strategy.FormattingStrategy;
import formatter.visitor.FormatterVisitorV1;

public class ArgumentsStrategy implements FormattingStrategy {
  private final FormattingStrategy whiteSpace;

  public ArgumentsStrategy(FormattingStrategy whiteSpace) {
    this.whiteSpace = whiteSpace;
  }

  @Override
  public String apply(AstNode node, FormatterVisitorV1 visitor) {
    CallExpression callExpression = (CallExpression) node;
    StringBuilder formattedCode = new StringBuilder();
    int argumentCount = callExpression.arguments().size();

    for (int i = 0; i < argumentCount; i++) {
      AstNode argument = callExpression.arguments().get(i);
      formattedCode.append(((FormatterVisitorV1) argument.accept(visitor)).getCurrentCode());
      if (i < argumentCount - 1) {
        formattedCode.append(",");
        formattedCode.append(whiteSpace.apply(node, visitor));
      }
    }
    return formattedCode.toString();
  }
}
