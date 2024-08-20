package parsers;

import ast.root.AstNode;
import ast.root.Program;
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
import token.types.TokenTagType;

public class Parser implements Progressable {
  private final List<StatementParser> statementParsers;
  private List<Observer> observers;
  private int totalStatements;
  private int processedStatements;

  public Parser(List<Observer> observers) {
    this.statementParsers =
        List.of(new CallFunctionParser(), new VariableDeclarationParser(), new AssignmentParser());
    this.observers = observers;
  }

  public Parser() {
    this.statementParsers =
        List.of(new CallFunctionParser(), new VariableDeclarationParser(), new AssignmentParser());
    this.observers = null;
  }

  public Program parse(List<Token> tokens) {
    List<List<Token>> statements = splitBySemicolon(tokens);

    List<AstNode> astNodes = new ArrayList<>();

    StatementParser parser;

    totalStatements = statements.size();
    processedStatements = 0;

    for (List<Token> statement : statements) {
      parser = getValidParser(statement);
      AstNode astNode = parser.parse(statement);
      astNodes.add(astNode);
      processedStatements++;
      updateProgress();
    }
    Position start = tokens.get(0).getInitialPosition();
    Position end = tokens.get(tokens.size() - 1).getFinalPosition();

    return new Program(astNodes, start, end);
  }

  private static List<List<Token>> splitBySemicolon(List<Token> tokens) {
    List<List<Token>> result = new ArrayList<>();
    List<Token> currentSublist = new ArrayList<>();

    for (Token token : tokens) {
      if (token.getType() == TokenTagType.SEMICOLON) {
        result.add(new ArrayList<>(currentSublist));
        currentSublist.clear();
      } else {
        currentSublist.add(token);
      }
    }

    // Checks if the statement ends with a semicolon
    if (!tokens.isEmpty() && tokens.get(tokens.size() - 1).getType() != TokenTagType.SEMICOLON) {
      throw new IllegalArgumentException("Error: Statement does not end with a semicolon");
    }

    return result;
  }

  private StatementParser getValidParser(List<Token> statement) {
    for (StatementParser statementParser : statementParsers) {
      if (statementParser.shouldParse(statement)) {
        return statementParser;
      }
    }
    // TODO find a better way to throw the error
    throw new IllegalArgumentException("No parser found for this statement");
  }

  private void updateProgress() {
    if (observers != null) {
      notifyObservers();
    }
  }

  @Override
  public int getProgress() {
    return (int) (((double) processedStatements / totalStatements) * 100);
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
