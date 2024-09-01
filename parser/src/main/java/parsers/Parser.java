package parsers;

import static exceptions.ExceptionMessageBuilder.getExceptionMessage;

import ast.root.AstNode;
import ast.root.Program;
import ast.splitters.StatementSplitter;
import exceptions.UnsupportedStatementException;
import java.util.ArrayList;
import java.util.List;
import observers.Observer;
import observers.Progressable;
import parsers.statements.AssignmentParser;
import parsers.statements.CallFunctionParser;
import parsers.statements.StatementParser;
import parsers.statements.VariableDeclarationParser;
import token.Position;
import token.Token;

public class Parser implements Progressable {
  private final List<StatementParser> statementParsers;
  private final List<Observer> observers;
  private final StatementSplitter statementSplitter;
  private int totalStatements;

  public Parser(List<Observer> observers) {
    this.statementParsers =
        List.of(new CallFunctionParser(), new VariableDeclarationParser(), new AssignmentParser());
    this.observers = observers;
    this.statementSplitter = new StatementSplitter();
  }

  public Parser() {
    this.statementParsers =
        List.of(new CallFunctionParser(), new VariableDeclarationParser(), new AssignmentParser());
    this.observers = List.of();
    this.statementSplitter = new StatementSplitter();
  }

  public Program parse(List<Token> tokens) {
    List<List<Token>> statements = statementSplitter.split(tokens);

    List<AstNode> astNodes = new ArrayList<>();

    StatementParser parser;

    totalStatements = statements.size();

    for (List<Token> statement : statements) {
      parser = getValidParser(statement);
      AstNode astNode = parser.parse(statement);
      astNodes.add(astNode);
      updateProgress();
    }
    Position start = tokens.get(0).initialPosition();
    Position end = tokens.get(tokens.size() - 1).finalPosition();

    return new Program(astNodes, start, end);
  }

  private StatementParser getValidParser(List<Token> statement) {
    for (StatementParser statementParser : statementParsers) {
      if (statementParser.shouldParse(statement)) {
        return statementParser;
      }
    }

    Token token = statement.get(0);
    Position position = token.initialPosition();
    String exceptionMessage = getExceptionMessage(token.value(), position.row(), position.col());
    throw new UnsupportedStatementException(exceptionMessage);
  }

  private void updateProgress() {
    if (observers != null) {
      notifyObservers();
    }
  }

  @Override
  public float getProgress() {
    return ((float) 1 / totalStatements) * 100;
  }

  @Override
  public void notifyObservers() {
    for (Observer observer : observers) {
      observer.update(this);
    }
  }
}
