package output;

import report.Report;

public class OutputReportSystem implements OutputResult<Report> {
  @Override
  public void saveResult(Report result) {
    System.out.println(result);
  }

  @Override
  public Report getResult() {
    return null;
  }
}
