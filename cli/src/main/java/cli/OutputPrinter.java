package cli;

import output.OutputResult;

public class OutputPrinter implements OutputResult {
  @Override
  public OutputResult saveResult(String result) {
    System.out.println(result);
    return this;
  }

  @Override
  public String getResult() {
    return "";
  }
}
