package linter;

import ast.root.AstNode;
import ast.root.Program;
import ast.visitor.NodeVisitor;
import java.util.List;
import linter.report.FullReport;
import linter.visitor.LinterVisitor;
import linter.visitor.factory.LinterVisitorFactory;
import observers.Observer;
import observers.Progressable;

public class Linter implements Progressable {
  private final LinterVisitorFactory linterVisitorFactory = new LinterVisitorFactory();
  private final List<Observer> observers;
  private int totalSteps;

  public Linter() {
    this.observers = List.of();
  }

  public Linter(List<Observer> observer) {
    this.observers = observer;
  }

  public FullReport lint(Program program, String rules) {
    NodeVisitor linterVisitor = linterVisitorFactory.createLinterVisitor(rules);

    List<AstNode> statements = program.statements();
    totalSteps = statements.size();

    for (AstNode statement : statements) {
      linterVisitor = statement.accept(linterVisitor);
      notifyObservers();
    }

    return ((LinterVisitor) linterVisitor).getFullReport();
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
