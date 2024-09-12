package interpreter;

import ast.root.AstNode;
import factory.ParserFactory;
import interpreter.visitor.InterpreterVisitor;
import interpreter.visitor.InterpreterVisitorFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import lexer.Lexer;
import parsers.Parser;

public class IterableInterpreter implements Iterator<List<String>> {
  private InterpreterVisitor nodeVisitor;
  private final Parser parser;

  public IterableInterpreter(String version, InputStream code) {
    this.nodeVisitor = InterpreterVisitorFactory.getInterpreterVisitor(version);
    this.parser = getParser(version, code);
  }

  @Override
  public boolean hasNext() {
    return parser.hasNext();
  }

  @Override
  public List<String> next() {
    AstNode statement = parser.next();
    nodeVisitor = (InterpreterVisitor) statement.accept(nodeVisitor.cloneVisitor());
    return nodeVisitor.getPrintedValues();
  }

  private Parser getParser(String version, InputStream code) {
    Parser parser = ParserFactory.getParser(version);
    Lexer newLexer = null;
    try {
      newLexer = parser.getLexer().setInput(code);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return parser.setLexer(newLexer);
  }
}
