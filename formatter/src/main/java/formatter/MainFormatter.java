package formatter;

import ast.root.AstNode;
import ast.root.Program;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import formatter.statement.Formatter;
import java.util.List;
import observers.Observer;
import observers.Progressable;

public class MainFormatter implements Progressable {
  private final List<Formatter> formatters;
  private final List<Observer> observers;
  private int totalSteps;

  public MainFormatter(List<Formatter> formatters, List<Observer> observers) {
    this.formatters = formatters;
    this.observers = observers;
  }

  public MainFormatter(List<Formatter> formatters) {
    this.formatters = formatters;
    this.observers = List.of();
  }

  public String format(Program program, String options) {
    JsonObject jsonOptions = JsonParser.parseString(options).getAsJsonObject();
    JsonObject rules = jsonOptions.getAsJsonObject("rules");
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
    }
    return formattedCode.toString();
  }

  /*
  private Formatter getValidFormatter(AstNode statement) {
    for (Formatter formatter : formatters) {
      if (formatter.shouldFormat(statement)) {
        return formatter;
      }
    }
    throw new IllegalArgumentException("No formatter found for this statement");
  }

   */

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
