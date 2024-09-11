package formatter;

import formatter.visitor.FormatterVisitor;
import java.util.Iterator;
import java.util.List;
import observers.Observer;
import observers.Progressable;
import parsers.Parser;

public class MainFormatter implements Progressable, Iterator<String> {
  private final List<Observer> observers;
  private int totalSteps;
  private FormatterVisitor visitor;
  private final Parser parser;

  public MainFormatter(List<Observer> observers, FormatterVisitor visitor, Parser parser) {
    this.observers = observers;
    this.visitor = visitor;
    this.parser = parser;
  }

  public String formatProgram() {
    StringBuilder formattedCode = new StringBuilder();
    while (hasNext()) {
      formattedCode.append(next());
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

  @Override
  public boolean hasNext() {
    return parser.hasNext();
  }

  @Override
  public String next() {
    visitor = (FormatterVisitor) parser.next().accept(visitor);
    return visitor.getCurrentCode();
  }
}
