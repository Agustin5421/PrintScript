package formatter.strategy.common;

import ast.root.AstNode;
import formatter.strategy.FormattingStrategy;
import formatter.visitor.FormatterVisitor;
import java.util.List;

public class ArgumentsStrategy implements FormattingStrategy {
  // List of three white spaces, one before the first argument,
  // one after all the comas and one after the last argument
  private final List<CharacterStrategy> whiteSpaces;
  private final List<AstNode> arguments;

  public ArgumentsStrategy(List<CharacterStrategy> whiteSpaces, List<AstNode> arguments) {
    this.whiteSpaces = whiteSpaces;
    this.arguments = arguments;
  }

  public ArgumentsStrategy(List<CharacterStrategy> whiteSpaces) {
    this(whiteSpaces, List.of());
  }

  public ArgumentsStrategy newStrategy(List<AstNode> arguments) {
    return new ArgumentsStrategy(whiteSpaces, arguments);
  }

  @Override
  public String apply(AstNode node, FormatterVisitor visitor) {
    StringBuilder formattedCode = new StringBuilder();
    CharacterStrategy comaSpace = whiteSpaces.get(1);
    int argumentsCount = arguments.size();

    formattedCode.append(whiteSpaces.get(0).apply(node, visitor));
    for (int i = 0; i < argumentsCount; i++) {
      AstNode argument = arguments.get(i);
      formattedCode.append(((FormatterVisitor) argument.accept(visitor)).getCurrentCode());
      if (i < argumentsCount - 1) {
        formattedCode.append(",");
        formattedCode.append(comaSpace.apply(node, visitor));
      }
    }
    formattedCode.append(whiteSpaces.get(2).apply(node, visitor));
    return formattedCode.toString();
  }
}
