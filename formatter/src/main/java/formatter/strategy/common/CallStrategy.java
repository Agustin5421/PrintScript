package formatter.strategy.common;

import ast.root.AstNode;
import formatter.FormattingEngine;
import formatter.strategy.FormattingStrategy;
import java.util.List;

public class CallStrategy implements FormattingStrategy {
  private final FormattingStrategy lineBreaksStrategy;
  // The white spaces that goes between the keyword and ()
  private final String whiteSpace;
  private final String keyWord;
  private final List<AstNode> arguments;
  private final ArgumentsStrategy argumentsStrategy;
  private final CharacterStrategy end;

  public CallStrategy(
      FormattingStrategy lineBreaksStrategy,
      String whiteSpace,
      String keyWord,
      List<AstNode> arguments,
      ArgumentsStrategy argumentsStrategy,
      CharacterStrategy end) {
    this.lineBreaksStrategy = lineBreaksStrategy;
    this.whiteSpace = whiteSpace;
    this.keyWord = keyWord;
    this.arguments = arguments;
    this.argumentsStrategy = argumentsStrategy;
    this.end = end;
  }

  public CallStrategy(
      FormattingStrategy lineBreaksStrategy,
      String whiteSpace,
      ArgumentsStrategy argumentsStrategy,
      CharacterStrategy end) {
    this(lineBreaksStrategy, whiteSpace, "", List.of(), argumentsStrategy, end);
  }

  public CallStrategy newStrategy(String keyWord, List<AstNode> arguments) {
    return new CallStrategy(
        lineBreaksStrategy, whiteSpace, keyWord, arguments, argumentsStrategy, end);
  }

  @Override
  public FormattingEngine apply(AstNode node, FormattingEngine engine) {
    // Print function is the only one for now that has this strategy
    if (keyWord.equals("println")) {
      lineBreaksStrategy.apply(node, engine);
    }
    engine.write(keyWord);
    engine.write(whiteSpace);
    engine.write("(");
    ArgumentsStrategy newArgsStrategy = argumentsStrategy.newStrategy(arguments);
    newArgsStrategy.apply(node, engine);
    engine.write(")");
    if (keyWord.equals("println")) {
      engine.write(";\n");
    }
    end.apply(node, engine);
    return engine;
  }
}
