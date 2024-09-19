package formatter.context;

public class NoContext implements FormattingContext {
  @Override
  public String addContext() {
    return "";
  }

  @Override
  public FormattingContext changeContext() {
    return this;
  }
}
