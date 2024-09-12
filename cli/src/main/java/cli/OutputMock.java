package cli;


import output.OutputResult;

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
