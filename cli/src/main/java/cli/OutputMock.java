package cli;

import runner.OutputResult;

public class OutputMock implements OutputResult {
  @Override
  public OutputResult saveResult(String result) {
    return this;
  }

  @Override
  public String getResult() {
    return "";
  }
}
