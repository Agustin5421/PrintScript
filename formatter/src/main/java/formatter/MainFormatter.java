package formatter;

import ast.root.AstNode;
import ast.root.Program;
import java.util.List;
import observers.Observer;
import observers.Progressable;

public class MainFormatter implements Progressable {
  private final List<Observer> observers;
  private int totalSteps;
  private FormatterVisitor visitor;

  public MainFormatter(List<Observer> observers, FormatterVisitor visitor) {
    this.observers = observers;
    this.visitor = visitor;
  }

  public String format(Program program) {
    StringBuilder formattedCode = new StringBuilder();
    totalSteps = program.statements().size();
    for (AstNode statement : program.statements()) {
      visitor = (FormatterVisitor) statement.accept(visitor);
      formattedCode.append(visitor.getCurrentCode());
      notifyObservers();
    }
    return formattedCode.toString();
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
