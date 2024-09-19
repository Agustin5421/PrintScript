package formatter.context;

public class IndentationContext implements FormattingContext {
  private final int indentSize;
  private final int indentationLevel;

  public IndentationContext(int indentSize, int indentationLevel) {
    this.indentSize = indentSize;
    this.indentationLevel = indentationLevel;
  }

  public int getIndentation() {
    return indentationLevel * indentSize;
  }

  @Override
  public String addContext() {
    return "\t".repeat(getIndentation());
  }

  @Override
  public FormattingContext changeContext() {
    return new IndentationContext(indentSize, indentationLevel + 1);
  }
}
