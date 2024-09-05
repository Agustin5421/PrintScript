package parsers;

import ast.expressions.Expression;
import ast.root.AstNode;
import ast.splitters.StatementSplitter;
import ast.statements.Statement;
import exceptions.UnsupportedExpressionException;
import exceptions.UnsupportedStatementException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lexer.Lexer;
import parsers.expressions.ExpressionParser;
import parsers.statements.StatementParser;
import token.Token;

public class Parser implements Iterator<AstNode> {
  private final List<StatementParser> statementParsers;
  private final List<ExpressionParser> expressionParsers;
  private final StatementSplitter statementSplitter;
  private final Lexer lexer;

  public Parser(
      Lexer lexer,
      List<StatementParser> statementParsers,
      List<ExpressionParser> expressionParsers,
      StatementSplitter statementSplitter) {
    this.lexer = lexer;
    this.statementParsers = statementParsers;
    this.expressionParsers = expressionParsers;
    this.statementSplitter = statementSplitter;
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
    List<List<Token>> statements = statementSplitter.split(tokens);
    List<Statement> astNodes = new ArrayList<>();

    for (List<Token> statement : statements) {
      astNodes.add(parseStatement(statement));
    }

    return astNodes;
  }

  @Override
  public boolean hasNext() {
    return false;
  }

  @Override
  public AstNode next() {
    List<Token> tokens = new ArrayList<>();

    while (!isStatement(tokens)) {
      if (lexer.hasNext()) {
        tokens.add(lexer.next());
      } else {
        throw new UnsupportedStatementException(tokens.toString());
      }
    }

    return parseStatement(tokens);
  }

  private boolean isStatement(List<Token> tokens) {
    try {
      statementSplitter.split(tokens);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public Parser setLexer(Lexer lexer) {
    return new Parser(lexer, statementParsers, expressionParsers, statementSplitter);
  }

  public Lexer getLexer() {
    return lexer;
  }
}
