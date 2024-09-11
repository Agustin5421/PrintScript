package formatter.strategy.common;

import ast.root.AstNode;
import formatter.strategy.FormattingStrategy;
import formatter.visitor.FormatterVisitor;
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
  public String apply(AstNode node, FormatterVisitor visitor) {
    StringBuilder formattedCode = new StringBuilder();
    formattedCode.append(lineBreaksStrategy.apply(node, visitor));
    formattedCode.append(keyWord);
    formattedCode.append(whiteSpace);
    formattedCode.append("(");
    ArgumentsStrategy newArgsStrategy = argumentsStrategy.newStrategy(arguments);
    formattedCode.append(newArgsStrategy.apply(node, visitor));
    formattedCode.append(")");
    formattedCode.append(end.apply(node, visitor));
    return formattedCode.toString();
  }
}
