package linter;

import ast.root.AstNode;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import lexer.Lexer;
import linter.visitor.LinterVisitor;
import linter.visitor.report.FullReport;
import parsers.Parser;

public class Linter implements Iterator<FullReport> {
  private final Parser parser;
  private final LinterVisitor linterVisitor;

  public Linter(Parser parser, LinterVisitor linterVisitor) {
    this.parser = parser;
    this.linterVisitor = linterVisitor;
  }

  public Linter setInputStream(InputStream code) {
    Parser parser = getParser();
    Lexer newLexer;
    try {
      newLexer = parser.getLexer().setInput(code);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return setParser(parser.setLexer(newLexer));
  }

  public Linter setInput(String code) {
    Parser parser = getParser();
    Lexer newLexer = parser.getLexer().setInputAsString(code);
    return setParser(parser.setLexer(newLexer));
  }

  public FullReport lint(AstNode astNode) {
    LinterVisitor visitor = (LinterVisitor) linterVisitor.visit(astNode);
    return visitor.getFullReport();
  }

  public Parser getParser() {
    return parser;
  }

  public Linter setParser(Parser parser) {
    return new Linter(parser, linterVisitor);
  }

  @Override
  public boolean hasNext() {
    return parser.hasNext();
  }

  @Override
  public FullReport next() {
    if (!hasNext()) {
      throw new RuntimeException("No more code to lint.");
    }

    AstNode astNode = parser.next();

    return lint(astNode);
  }
}
