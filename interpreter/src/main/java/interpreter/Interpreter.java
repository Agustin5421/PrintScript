package interpreter;

import ast.root.AstNode;
import ast.visitor.NodeVisitor;
import factory.ParserFactory;
import interpreter.visitor.InterpreterVisitor;
import interpreter.visitor.InterpreterVisitorFactory;
import interpreter.visitor.InterpreterVisitorV2;
import interpreter.visitor.repository.VariablesRepository;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import lexer.Lexer;
import observers.Observer;
import observers.Progressable;
import output.OutputResult;
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
      NodeVisitor visitor1 = (InterpreterVisitor) statement.accept(visitor);
      visitor = mergeVisitors(visitor, (NodeVisitor) visitor1);
      updateProgress();
    }

    return visitor.getPrintedValues();
  }

  public void interpretInputStream(InputStream code, OutputResult printLog) {

    InterpreterVisitor visitor = nodeVisitor;
    Lexer newLexer = null;
    try {
      newLexer = parser.getLexer().setInput(code);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    parser = parser.setLexer(newLexer);
    while (hasMoreStatements()) {
      AstNode statement = getNextStatement();
      visitor = (InterpreterVisitor) statement.accept(visitor.cloneVisitor());
      List<String> printedValues = visitor.getPrintedValues();
      for (String printedValue : printedValues) {
        printLog.saveResult(printedValue);
      }
      System.gc();
      updateProgress();
    }
  }

  private InterpreterVisitor mergeVisitors(NodeVisitor visitor, NodeVisitor nestedVisitor) {
    List<String> printedValues = ((InterpreterVisitor) visitor).getPrintedValues();
    printedValues.addAll(((InterpreterVisitor) nestedVisitor).getPrintedValues());

    VariablesRepository variablesRepository =
        ((InterpreterVisitor) visitor).getVariablesRepository();
    VariablesRepository nestedVisitorVariablesRepository =
        ((InterpreterVisitor) nestedVisitor).getVariablesRepository();

    // TODO: update var logic

    return new InterpreterVisitorV2(
        InterpreterVisitorFactory.getInterpreterVisitor("1.0"),
        nestedVisitorVariablesRepository,
        printedValues);
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
