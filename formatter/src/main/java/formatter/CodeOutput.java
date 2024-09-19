package formatter;

import output.OutputResult;

public class CodeOutput implements OutputResult<String> {
  private final StringBuilder formattedCode = new StringBuilder();

  @Override
  public void saveResult(String result) {
    formattedCode.append(result);
  }

  @Override
  public String getResult() {
    return formattedCode.toString();
  }
}
