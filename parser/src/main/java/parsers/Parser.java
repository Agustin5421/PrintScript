package parsers;

import ast.expressions.Expression;
import ast.root.AstNode;
import ast.statements.Statement;
import exceptions.UnsupportedExpressionException;
import exceptions.UnsupportedStatementException;
import java.util.Iterator;
import java.util.List;
import lexer.Lexer;
import parsers.expressions.ExpressionParser;
import parsers.statements.StatementParser;
import splitters.MainStatementValidator;
import token.Token;

public class Parser implements Iterator<AstNode> {
  private final List<StatementParser> statementParsers;
  private final List<ExpressionParser> expressionParsers;
  private final MainStatementValidator mainStatementValidator;
  private final Lexer lexer;

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

    throw new UnsupportedStatementException(tokens.toString());
  }

  public List<Statement> parseBlock(List<Token> tokens) {
    // TODO: Implement block parsing
    throw new RuntimeException("Not implemented");
  }

  @Override
  public boolean hasNext() {
    // TODO: Implement hasNext correctly
    return lexer.hasNext();
  }

  @Override
  public AstNode next() {
    if (!hasNext()) {
      throw new RuntimeException("No more tokens to parse");
    }

    List<Token> statement = getNextStatement();
    return parseStatement(statement);
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
