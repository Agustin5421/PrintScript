package formatter.strategy.callexpr;

import ast.root.AstNode;
import formatter.FormatterVisitor;
import formatter.strategy.FormattingStrategy;

public class LineBreaksStrategy implements FormattingStrategy {
  private final int lineBreaks;

  public LineBreaksStrategy(int lineBreaks) {
    this.lineBreaks = lineBreaks;
  }

  @Override
  public String apply(AstNode node, FormatterVisitor visitor) {
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
