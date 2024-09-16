package output;

public class OutputMock implements OutputResult<String> {
  @Override
  public void saveResult(String result) {}

  @Override
  public String getResult() {
    return "";
  }
}
