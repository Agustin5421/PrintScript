package output;

import report.FullReport;
import report.Report;

public class OutputReport implements OutputResult<Report> {
  private FullReport fullReport = new FullReport();

  @Override
  public void saveResult(Report result) {
    fullReport = fullReport.addReport(result);
  }

  @Override
  public Report getResult() {
    return null;
  }

  public FullReport getFullReport() {
    return fullReport;
  }
}
