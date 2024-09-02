package parsers;

import static exceptions.ExceptionMessageBuilder.getExceptionMessage;

import ast.expressions.Expression;
import ast.root.AstNode;
import ast.root.Program;
import ast.splitters.StatementSplitter;
import ast.statements.Statement;
import exceptions.UnsupportedExpressionException;
import exceptions.UnsupportedStatementException;
import java.util.ArrayList;
import java.util.List;
import observers.Observer;
import observers.Progressable;
import parsers.expressions.ExpressionParser;
import parsers.statements.AssignmentParser;
import parsers.statements.CallFunctionParser;
import parsers.statements.StatementParser;
import parsers.statements.VariableDeclarationParser;
import token.Position;
import token.Token;

public class Parser implements Progressable {
  private final List<StatementParser> statementParsers;
  private final List<ExpressionParser> expressionParsers;
  private final List<Observer> observers;
  private final StatementSplitter statementSplitter;
  private int totalStatements;
  // Total statements used to be needed to calculate progress
  // After changing modules to only use one statement, this is no longer needed
  // Since I didn't update the progress calculation
  public Parser(List<Observer> observers) {
    this.statementParsers =
        List.of(new CallFunctionParser(), new VariableDeclarationParser(), new AssignmentParser());
    this.observers = observers;
    this.statementSplitter = new StatementSplitter();
    this.expressionParsers = List.of();
  }

  public Parser() {
    this.statementParsers =
        List.of(new CallFunctionParser(), new VariableDeclarationParser(), new AssignmentParser());
    this.observers = List.of();
    this.statementSplitter = new StatementSplitter();
    this.expressionParsers = List.of();
  }

  public Parser(
      List<StatementParser> statementParsers,
      List<ExpressionParser> expressionParsers,
      StatementSplitter statementSplitter) {
    this.statementParsers = statementParsers;
    this.expressionParsers = expressionParsers;
    this.observers = List.of();
    this.statementSplitter = statementSplitter;
  }

  public Program parse(List<Token> tokens) {
    List<List<Token>> statements = statementSplitter.split(tokens);

    List<AstNode> astNodes = new ArrayList<>();

    for (List<Token> statement : statements) {
      StatementParser parser = getValidParser(statement);
      AstNode astNode = parser.parse(this, statement);
      astNodes.add(astNode);
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

  public Expression parseExpression(List<Token> tokens) {
    for (ExpressionParser expressionParser : expressionParsers) {
      if (expressionParser.shouldParse(tokens)) {
        return expressionParser.parse(this, tokens);
      }
    }

    // TODO: Add exception message
    throw new UnsupportedExpressionException(tokens.toString());
  }

  public Statement parseStatement(List<Token> tokens) {
    for (StatementParser statementParser : statementParsers) {
      if (statementParser.shouldParse(tokens)) {
        return statementParser.parse(this, tokens);
      }
    }

    throw new UnsupportedStatementException("Unsupported statement");
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
