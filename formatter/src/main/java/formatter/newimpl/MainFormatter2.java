package formatter.newimpl;

import ast.root.AstNode;
import ast.root.Program;
import java.util.List;
import observers.Observer;
import observers.Progressable;

public class MainFormatter2 implements Progressable {
  private final List<Observer> observers;
  private int totalSteps;
  private FormatterVisitor2 visitor;

  public MainFormatter2(List<Observer> observers, FormatterVisitor2 visitor) {
    this.observers = observers;
    this.visitor = visitor;
  }

  public String format(Program program, String options) {
    StringBuilder formattedCode = new StringBuilder();
    totalSteps = program.statements().size();
    for (AstNode statement : program.statements()) {
      visitor = (FormatterVisitor2) statement.accept(visitor);
      formattedCode
          .append(visitor.getCurrentCode())
          // Entering a new line after each statement
          .append("\n");
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
