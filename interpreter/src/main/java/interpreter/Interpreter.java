package interpreter;

import ast.root.AstNode;
import ast.visitor.NodeVisitor;
import factory.ParserFactory;
import interpreter.visitor.InterpreterVisitor;
import interpreter.visitor.InterpreterVisitorFactory;
import interpreter.visitor.repository.VariablesRepository;
import java.util.List;
import lexer.Lexer;
import observers.Observer;
import observers.Progressable;
import parsers.Parser;

public class Interpreter implements Progressable {
  private final List<Observer> observers;
  private int totalStatements;
  private final InterpreterVisitor nodeVisitor;

  private Parser parser;

  public Interpreter(List<Observer> observers, String version) {
    this.observers = observers;
    this.nodeVisitor = InterpreterVisitorFactory.getInterpreterVisitor(version);
    this.parser = ParserFactory.getParser(version);
  }

  // This constructor is created in order to make the tests pass
  public Interpreter(String version) {
    this.nodeVisitor = InterpreterVisitorFactory.getInterpreterVisitor(version);
    this.parser = ParserFactory.getParser(version);
    this.observers = List.of();
  }

  public Interpreter(Parser parser, InterpreterVisitor interpreterVisitor) {
    this.nodeVisitor = interpreterVisitor;
    this.parser = parser;
    this.observers = List.of();
  }

  // TODO: Delete return of executeProgram() method.
  // Testing purposes only.
  public VariablesRepository executeProgram(String code) {
    VariablesRepository variablesRepository = new VariablesRepository();
    InterpreterVisitor visitor = nodeVisitor;
    Lexer newLexer = parser.getLexer().setInputAsString(code);
    parser = parser.setLexer(newLexer);
    while (hasMoreStatements()) {
      AstNode statement = getNextStatement();
      visitor = (InterpreterVisitor) statement.accept(visitor);
      variablesRepository = visitor.getVariablesRepository();
      updateProgress();
    }

    return variablesRepository;
  }

  public List<String> interpret(String code) {
    InterpreterVisitor visitor = nodeVisitor;
    Lexer newLexer = parser.getLexer().setInputAsString(code);
    parser = parser.setLexer(newLexer);
    while (hasMoreStatements()) {
      AstNode statement = getNextStatement();
      visitor = (InterpreterVisitor) statement.accept(visitor);
      updateProgress();
    }

    return visitor.getPrintedValues();
  }

  private boolean hasMoreStatements() {
    return parser.hasNext();
  }

  private AstNode getNextStatement() {
    return parser.next();
  }

  public NodeVisitor getVisitor() {
    return nodeVisitor;
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
