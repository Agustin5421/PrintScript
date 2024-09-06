package interpreter;

import ast.root.AstNode;
import ast.root.Program;
import interpreter.visitor.InterpreterVisitor;
import java.util.List;
import observers.Observer;
import observers.Progressable;

public class Interpreter implements Progressable {
  private final List<Observer> observers;
  private int totalStatements;
  private final InterpreterVisitor nodeVisitor;

  public Interpreter(List<Observer> observers, InterpreterVisitor nodeVisitor) {
    this.observers = observers;
    this.nodeVisitor = nodeVisitor;
  }

  // This constructor is created in order to make the tests pass
  public Interpreter(InterpreterVisitor nodeVisitor) {
    this.nodeVisitor = nodeVisitor;
    this.observers = List.of();
  }

  // TODO: Delete return of executeProgram() method.

  public VariablesRepository executeProgram(Program program) {
    VariablesRepository variablesRepository = new VariablesRepository();
    InterpreterVisitor visitor = nodeVisitor;

    totalStatements = program.statements().size();

    for (AstNode statement : program.statements()) {
      visitor = (InterpreterVisitor) statement.accept(visitor);
      variablesRepository = visitor.getVariablesRepository();
      updateProgress();
    }

    return variablesRepository;
  }

  private void updateProgress() {
    notifyObservers();
  }

  @Override
  public void notifyObservers() {
    for (Observer observer : observers) {
      observer.update(this);
    }
  }

  @Override
  public float getProgress() {
    return ((float) 1 / totalStatements) * 100;
  }
}
