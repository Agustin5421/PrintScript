package linter.report;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import position.Position;
import report.FullReport;
import report.Report;

public class ReportTest {
  @Test
  public void testToString() {
    Position position = new Position(0, 0);
    Report report = new Report(position, position, "test");

    assertEquals("Warning from (row: 0, col: 0) to (row: 0, col: 0): test", report.toString());
  }

  @Test
  public void testToString2() {
    Position position = new Position(0, 0);
    Report report = new Report(position, position, "test");
    FullReport fullReport = new FullReport().addReport(report).addReport(report);

    assertEquals(
        "Warning from (row: 0, col: 0) to (row: 0, col: 0): test\n"
            + "Warning from (row: 0, col: 0) to (row: 0, col: 0): test",
        fullReport.toString());
  }
}
