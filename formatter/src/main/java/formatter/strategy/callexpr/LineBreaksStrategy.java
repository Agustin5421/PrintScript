package formatter.strategy.callexpr;

import ast.root.AstNode;
import formatter.FormattingEngine;
import formatter.strategy.FormattingStrategy;

public class LineBreaksStrategy implements FormattingStrategy {
  private final int lineBreaks;

  public LineBreaksStrategy(int lineBreaks) {
    this.lineBreaks = lineBreaks + 1;
  }

  @Override
  public FormattingEngine apply(AstNode node, FormattingEngine engine) {
    String currentCode = engine.getCurrentCode();
    if (currentCode.isEmpty()) {
      return engine;
    }

    int existingLineBreaks = 0;

    // Count the number of line breaks at the end of the current code
    for (int i = currentCode.length() - 1; i >= 0; i--) {
      if (currentCode.charAt(i) == '\t') {
        continue;
      }
      if (currentCode.charAt(i) != '\n') {
        break;
      }
      existingLineBreaks++;
    }

    // Calculate the number of additional line breaks needed
    int additionalLineBreaks = Math.max(0, lineBreaks - existingLineBreaks);

    // Append the necessary number of line breaks with context
    for (int i = 0; i < additionalLineBreaks; i++) {
      engine.write("\n");
      engine.addContext();
    }

    return engine;
  }
}
