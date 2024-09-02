package factory;

import ast.identifier.IdentifierParser;
import ast.splitters.StatementSplitter;
import java.util.List;
import parsers.Parser;
import parsers.expressions.BinaryExpressionParser;
import parsers.expressions.ExpressionParser;
import parsers.expressions.LiteralParser;
import parsers.statements.AssignmentParser;
import parsers.statements.CallFunctionParser;
import parsers.statements.StatementParser;
import parsers.statements.VariableDeclarationParser;

public class ParserFactory {
  public static Parser getParser(String version) {
    return switch (version) {
      case "1.0" -> getParserV1();
      case "1.1" -> getParserV2();
      default -> throw new IllegalArgumentException("Invalid version: " + version);
    };
  }

  private static Parser getParserV1() {
    List<StatementParser> statementParsers =
        List.of(new CallFunctionParser(), new VariableDeclarationParser(), new AssignmentParser());
    List<ExpressionParser> expressionParsers =
        List.of(new IdentifierParser(), new LiteralParser(), new BinaryExpressionParser());
    return new Parser(statementParsers, expressionParsers, new StatementSplitter());
  }

  private static Parser getParserV2() {
    return null;
  }
}
