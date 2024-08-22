package linter;

import ast.root.AstNode;
import ast.root.Program;
import ast.visitor.NodeVisitor;
import java.util.List;
import visitors.LinterVisitor;

public class Linter {
  public String linter(Program program, String rules) {
    NodeVisitor visitor = new LinterVisitor(rules);

    List<AstNode> statements = program.statements();

    for (AstNode statement : statements) {
      visitor = statement.accept(visitor);
    }

    String report = ((LinterVisitor) visitor).getReport();
    report = trimLastNewLine(report);
    return report;
  }

  private String trimLastNewLine(String report) {
    if (!report.isEmpty()) {
      if (report.charAt(report.length() - 1) == '\n') {
        String newReport = report.substring(0, report.length() - 1);
        return trimLastNewLine(newReport);
      }
    }
    return report;
  }
}
