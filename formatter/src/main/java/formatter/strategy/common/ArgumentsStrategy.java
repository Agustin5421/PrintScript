package formatter.strategy.common;

import ast.root.AstNode;
import formatter.FormattingEngine;
import formatter.strategy.FormattingStrategy;
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

  // This strategy is already inside the "parentheses"
  @Override
  public FormattingEngine apply(AstNode node, FormattingEngine engine) {
    CharacterStrategy comaSpace = whiteSpaces.get(1);
    int argumentsCount = arguments.size();

    whiteSpaces.get(0).apply(node, engine);
    for (int i = 0; i < argumentsCount; i++) {
      AstNode argument = arguments.get(i);
      engine.format(argument);
      if (i < argumentsCount - 1) {
        engine.write(",");
        comaSpace.apply(node, engine);
      }
    }
    return whiteSpaces.get(2).apply(node, engine);
  }
}
