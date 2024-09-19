package parsers;

import ast.expressions.ExpressionNode;
import ast.root.AstNode;
import ast.statements.StatementNode;
import exceptions.UnsupportedExpressionException;
import exceptions.UnsupportedStatementException;
import java.util.Iterator;
import java.util.List;
import lexer.Lexer;
import parsers.expressions.ExpressionParser;
import parsers.statements.StatementParser;
import splitters.MainStatementSplitter;
import token.Token;
import validators.MainStatementValidator;
import visitor.ParsingValidatorVisitor;

public class Parser implements Iterator<AstNode> {
  private final List<StatementParser> statementParsers;
  private final List<ExpressionParser> expressionParsers;
  private final MainStatementValidator mainStatementValidator;
  private final MainStatementSplitter mainStatementSplitter = new MainStatementSplitter();
  private final Lexer lexer;
  private final ParsingValidatorVisitor visitor = new ParsingValidatorVisitor();

  public Parser(
      Lexer lexer,
      List<StatementParser> statementParsers,
      List<ExpressionParser> expressionParsers,
      MainStatementValidator statementSplitter) {
    this.lexer = lexer;
    this.statementParsers = statementParsers;
    this.expressionParsers = expressionParsers;
    this.mainStatementValidator = statementSplitter;
  }

  public AstNode parse(List<Token> tokens) {
    return parseStatement(tokens);
  }

  public ExpressionNode parseExpression(List<Token> tokens) {
    for (ExpressionParser expressionParser : expressionParsers) {
      if (expressionParser.shouldParse(tokens)) {
        return expressionParser.parse(this, tokens);
      }
    }

    throw new UnsupportedExpressionException(tokens);
  }

  public StatementNode parseStatement(List<Token> tokens) {
    for (StatementParser statementParser : statementParsers) {
      if (statementParser.shouldParse(tokens)) {
        return statementParser.parse(this, tokens);
      }
    }

    throw new UnsupportedStatementException(tokens);
  }

  public List<StatementNode> parseBlock(List<Token> tokens) {
    if (tokens.isEmpty()) {
      return List.of();
    }

    List<List<Token>> statements = mainStatementSplitter.split(tokens);
    List<StatementNode> parsedStatements = new java.util.ArrayList<>();

    for (List<Token> statement : statements) {
      parsedStatements.add(parseStatement(statement));
    }
    return parsedStatements;
  }

  @Override
  public boolean hasNext() {
    return lexer.hasNext();
  }

  @Override
  public AstNode next() {
    if (!hasNext()) {
      throw new RuntimeException("No more tokens to parse");
    }

    List<Token> statement = getNextStatement();
    AstNode statementNode = parseStatement(statement);

    statementNode.accept(visitor);

    return statementNode;
  }

  private List<Token> getNextStatement() {
    List<Token> statement = mainStatementValidator.getNextStatement(lexer);

    if (statement.isEmpty()) {
      throw new RuntimeException("No statement found");
    }

    return statement;
  }

  public Parser setLexer(Lexer lexer) {
    return new Parser(lexer, statementParsers, expressionParsers, mainStatementValidator);
  }

  public Lexer getLexer() {
    return lexer;
  }
}
