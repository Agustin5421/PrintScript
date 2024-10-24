package output;

public class OutputStringSystem implements OutputResult<String> {
  @Override
  public void saveResult(String result) {
    System.out.println(result);
  }

  @Override
  public String getResult() {
    return "";
  }
}
