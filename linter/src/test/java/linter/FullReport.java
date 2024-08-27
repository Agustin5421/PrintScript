package linter;

import java.util.ArrayList;
import java.util.List;

public class FullReport {
  private final List<Report> reports;

  public FullReport(List<Report> reports) {
    this.reports = reports;
  }

  public List<Report> getReports() {
    return reports;
  }

  public FullReport addReport(Report report) {
    List<Report> newReports = new ArrayList<>(List.copyOf(reports));
    newReports.add(report);
    return new FullReport(newReports);
  }

  public FullReport addReports(List<Report> newReports) {
    List<Report> reports = new ArrayList<>(List.copyOf(this.reports));
    reports.addAll(newReports);
    return new FullReport(reports);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Report report : reports) {
      sb.append(report.toString()).append("\n");
    }

    try {
      sb.deleteCharAt(sb.length() - 1);
      return sb.toString();
    } catch (StringIndexOutOfBoundsException e) {
      return "";
    }
  }
}
