package linter;

import ast.root.AstNode;
import ast.root.Program;
import ast.visitor.NodeVisitor;
import java.util.List;
import observers.Observer;
import observers.Progressable;

public class Linter implements Progressable {
  private final List<Observer> observers;
  private int totalSteps;

  public Linter(List<Observer> observer) {
    this.observers = observer;
  }

  public Linter() {
    this.observers = List.of();
  }

  public String linter(Program program, String rules) {
    NodeVisitor visitor = new LinterVisitor(rules);

    List<AstNode> statements = program.statements();
    totalSteps = statements.size();

    for (AstNode statement : statements) {
      visitor = statement.accept(visitor);
      notifyObservers();
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

  @Override
  public float getProgress() {
    return (float) 1 / totalSteps * 100;
  }

  @Override
  public void notifyObservers() {
    for (Observer observer : observers) {
      observer.update(this);
    }
  }
}
