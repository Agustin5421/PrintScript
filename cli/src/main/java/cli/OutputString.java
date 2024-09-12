package cli;

import output.OutputResult;

public class OutputString implements OutputResult {
  private final String result;

  public OutputString() {
    this.result = "";
  }

  OutputString(String result) {
    this.result = result;
  }

  @Override
  public OutputResult saveResult(String result) {
    return new OutputString(this.result + result);
  }

  @Override
  public String getResult() {
    return result;
  }
}
