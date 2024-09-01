import factory.LexerFactory;
import lexer.Lexer;
import parsers.Parser;

public class ContextProvider {

  static Parser initBinaryExpressionParser() {
    return new Parser();
  }

  static Lexer initLexer() {
    return LexerFactory.getLexer("1.0");
  }
}
