package cli;


import output.OutputResult;

public class OutputMock implements OutputResult<String> {
  @Override
  public void saveResult(String result) {

  }

  @Override
  public String getResult() {
    return "";
  }
}
