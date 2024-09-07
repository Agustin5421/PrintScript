package cli;

import runner.OutputResult;

public class MockOutput implements OutputResult {
  @Override
  public OutputResult saveResult(String result) {
    return this;
  }

  @Override
  public String getResult() {
    return "";
  }
}
