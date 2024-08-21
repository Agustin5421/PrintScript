package interpreter;

import ast.root.AstNode;
import ast.root.Program;
import observers.Observer;
import observers.ProgressObserver;
import observers.ProgressPrinter;
import observers.Progressable;

import java.util.List;

public class Interpreter implements Progressable {
  private final List<Observer> observers;
  private int totalStatements;
  private int completedStatements;

  public Interpreter(List<Observer> observers) {
    this.observers = observers;
  }

  public Interpreter() {
    this.observers = List.of(new ProgressObserver(new ProgressPrinter()));
  }

  //  este es el anterior sin el visitor, lo dejo por si acaso
//  public VariablesRepository executeProgram(Program program) {
//    VariablesRepository variablesRepository = new VariablesRepository();
//    totalStatements = program.statements().size();
//    completedStatements = 0;
//
//    for (AstNode statement : program.statements()) {
//      variablesRepository = evaluateStatement(statement, variablesRepository);
//      completedStatements++;
//      updateProgress();
//    }
//
//    return variablesRepository;
//  }

  public VariablesRepository executeProgram(Program program) {
    VariablesRepository variablesRepository = new VariablesRepository();
    AstNodeVisitor nodeVisitor = new AstNodeVisitor(variablesRepository);
    totalStatements = program.statements().size();
    completedStatements = 0;

    for (AstNode statement : program.statements()) {
      statement.accept(nodeVisitor);
      variablesRepository = nodeVisitor.variablesRepository;
      completedStatements++;
      updateProgress();
    }

    return variablesRepository;
  }

  private void updateProgress() {
    assert observers != null;
    if (!observers.isEmpty()) {
      notifyObservers();
    }
  }

  @Override
  public int getProgress() {
    return (int) (((double) completedStatements / totalStatements) * 100);
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