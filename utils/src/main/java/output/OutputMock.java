package output;

public class OutputMock implements OutputResult<String> {
  @Override
  public void saveResult(String result) {
    return;
  }

  @Override
  public String getResult() {
    return "";
  }
}
