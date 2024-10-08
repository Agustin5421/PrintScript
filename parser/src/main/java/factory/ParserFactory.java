package factory;

import java.util.List;
import lexer.Lexer;
import parsers.Parser;
import parsers.expressions.BinaryExpressionParser;
import parsers.expressions.CallFunctionAsExpressionParser;
import parsers.expressions.ExpressionParser;
import parsers.expressions.IdentifierParser;
import parsers.expressions.LiteralParser;
import parsers.statements.AssignmentParser;
import parsers.statements.CallFunctionAsStatementParser;
import parsers.statements.IfParser;
import parsers.statements.StatementParser;
import parsers.statements.VariableDeclarationParser;
import validators.MainStatementValidator;

public class ParserFactory {
  public static Parser getParser(String version) {
    return switch (version) {
      case "1.0" -> getParserV1();
      case "1.1" -> getParserV2();
      default -> throw new IllegalArgumentException("Invalid version: " + version);
    };
  }

  private static Parser getParserV1() {
    Lexer lexer = LexerFactory.getLexer("1.0");
    List<StatementParser> statementParsers =
        List.of(
            new CallFunctionAsStatementParser(List.of("println")),
            new VariableDeclarationParser(List.of("let"), List.of("string", "number")),
            new AssignmentParser());
    List<ExpressionParser> expressionParsers =
        List.of(
            new IdentifierParser(),
            new LiteralParser(),
            new BinaryExpressionParser(),
            new CallFunctionAsExpressionParser(List.of("println")));
    return new Parser(lexer, statementParsers, expressionParsers, new MainStatementValidator());
  }

  private static Parser getParserV2() {
    Lexer lexer = LexerFactory.getLexer("1.1");
    List<StatementParser> statementParsers =
        List.of(
            new IfParser(),
            new CallFunctionAsStatementParser(List.of("println", "readInput", "readEnv")),
            new VariableDeclarationParser(
                List.of("let", "const"), List.of("string", "number", "boolean")),
            new AssignmentParser());
    List<ExpressionParser> expressionParsers =
        List.of(
            new IdentifierParser(),
            new LiteralParser(),
            new CallFunctionAsExpressionParser(List.of("println", "readInput", "readEnv")),
            new BinaryExpressionParser());
    return new Parser(lexer, statementParsers, expressionParsers, new MainStatementValidator());
  }
}
