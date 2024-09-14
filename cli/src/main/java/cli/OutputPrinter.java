package cli;

import output.OutputResult;

public class OutputPrinter implements OutputResult<String> {
  @Override
  public void saveResult(String result) {
    System.out.println(result);
  }

  @Override
  public String getResult() {
    return "";
  }
}
