package output;

import java.util.ArrayList;
import java.util.List;

public class OutputListString implements OutputResult<String> {
  private List<String> result = new ArrayList<>();

  public List<String> getSavedResults() {
    return result;
  }

  @Override
  public void saveResult(String result) {
    this.result.add(result);
  }

  @Override
  public String getResult() {
    return "";
  }
}
