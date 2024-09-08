package formatter.strategy.callexpr;

import ast.root.AstNode;
import formatter.strategy.FormattingStrategy;
import formatter.visitor.FormatterVisitorV1;

public class LineBreaksStrategy implements FormattingStrategy {
  private final int lineBreaks;

  public LineBreaksStrategy(int lineBreaks) {
    this.lineBreaks = lineBreaks + 1;
  }

  @Override
  public String apply(AstNode node, FormatterVisitorV1 visitor) {
    String currentCode = visitor.getCurrentCode();
    if (currentCode.isEmpty()) {
      return "";
    }

    int existingLineBreaks = 0;

    // Count the number of line breaks at the end of the current code
    for (int i = currentCode.length() - 1; i >= 0 && currentCode.charAt(i) == '\n'; i--) {
      existingLineBreaks++;
    }

    // Calculate the number of additional line breaks needed
    int additionalLineBreaks = Math.max(0, lineBreaks - existingLineBreaks);

    // Append the necessary number of line breaks
    return "\n".repeat(additionalLineBreaks);
  }
}
