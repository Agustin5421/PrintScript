package interpreter;

import ast.root.AstNode;
import ast.root.Program;
import interpreter.visitor.InterpreterVisitorV1;
import java.util.List;
import observers.Observer;
import observers.Progressable;

public class Interpreter implements Progressable {
  private final List<Observer> observers;
  private int totalStatements;

  public Interpreter(List<Observer> observers) {
    this.observers = observers;
  }

  // This constructor is created in order to make the tests pass
  public Interpreter() {
    this.observers = List.of();
  }

  // TODO: Delete return of executeProgram() method.

  public VariablesRepository executeProgram(Program program) {
    VariablesRepository variablesRepository = new VariablesRepository();
    InterpreterVisitorV1 nodeVisitor = new InterpreterVisitorV1(variablesRepository);
    totalStatements = program.statements().size();

    for (AstNode statement : program.statements()) {
      nodeVisitor = (InterpreterVisitorV1) statement.accept(nodeVisitor);
      variablesRepository = nodeVisitor.getVariablesRepository();
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
