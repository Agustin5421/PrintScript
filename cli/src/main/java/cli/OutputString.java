package cli;

import output.OutputResult;

public class OutputString implements OutputResult<String> {
  private String result;

  public OutputString() {
    this.result = "";
  }

  OutputString(String result) {
    this.result = result;
  }

  @Override
  public void saveResult(String result) {
    this.result += result;
  }

  @Override
  public String getResult() {
    return result;
  }
}
