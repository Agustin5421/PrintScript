package cli;

import runner.OutputResult;

public class StringOutput implements OutputResult {
  private final StringBuilder result = new StringBuilder();

  @Override
  public OutputResult saveResult(String result) {
    this.result.append(result);
    return this;
  }

  @Override
  public String getResult() {
    return result.toString();
  }
}
