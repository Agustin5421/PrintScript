package linter.rework;

import ast.root.AstNode;
import ast.root.Program;
import ast.visitor.NodeVisitor;
import java.util.List;
import linter.rework.report.FullReport;
import linter.rework.visitor.LinterVisitorFactory;
import linter.rework.visitor.LinterVisitorV2;
import observers.Observer;
import observers.Progressable;

public class LinterV2 implements Progressable {
  private final LinterVisitorFactory linterVisitorFactory = new LinterVisitorFactory();
  private final List<Observer> observers;
  private int totalSteps;

  public LinterV2() {
    this.observers = List.of();
  }

  public LinterV2(List<Observer> observer) {
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

    return ((LinterVisitorV2) linterVisitor).getFullReport();
  }

  @Override
  public float getProgress() {
    return (float) 1 / totalSteps * 100;
  }

  @Override
  public void addObserver(Observer observer) {
    observers.add(observer);
  }

  @Override
  public void removeObserver(Observer observer) {
    observers.remove(observer);
  }

  @Override
  public void notifyObservers() {
    for (Observer observer : observers) {
      observer.update(this);
    }
  }
}
