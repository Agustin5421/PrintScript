package formatter;

import ast.root.AstNode;
import ast.root.Program;
import com.google.gson.JsonObject;
import java.util.List;
import observers.Observer;
import observers.Progressable;

public class MainFormatter implements Progressable {
  private final List<Observer> observers;
  private final OptionsChecker optionsChecker;
  private int totalSteps;

  public MainFormatter(List<Observer> observers) {
    this.observers = observers;
    this.optionsChecker = new OptionsChecker();
  }

  public String format(Program program, String options) {
    JsonObject rules = optionsChecker.checkAndReturn(options);
    StringBuilder formattedCode = new StringBuilder();
    FormatterVisitor formatterVisitor;
    totalSteps = program.statements().size();
    for (AstNode statement : program.statements()) {
      // Initialize the formatterVisitor with the current program
      formatterVisitor = new FormatterVisitor(rules, formattedCode.toString());
      // Changing the current program
      formatterVisitor = (FormatterVisitor) statement.accept(formatterVisitor);
      formattedCode
          .append(formatterVisitor.getCurrentCode())
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
