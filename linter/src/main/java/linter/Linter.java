package linter;

import ast.root.AstNode;
import java.util.Iterator;
import lexer.Lexer;
import linter.visitor.LinterVisitor;
import linter.visitor.report.FullReport;
import observers.Progressable;
import parsers.Parser;

public class Linter implements Progressable, Iterator<FullReport> {
  private final Parser parser;
  private final LinterVisitor linterVisitor;

  public Linter(Parser parser, LinterVisitor linterVisitor) {
    this.parser = parser;
    this.linterVisitor = linterVisitor;
  }

  public Linter setInput(String code) {
    Parser parser = getParser();
    Lexer newLexer = parser.getLexer().setInputAsString(code);
    return setParser(parser.setLexer(newLexer));
  }

  public FullReport lint(AstNode astNode) {
    LinterVisitor visitor = (LinterVisitor) astNode.accept(linterVisitor);
    return visitor.getFullReport();
  }

  public Parser getParser() {
    return parser;
  }

  public Linter setParser(Parser parser) {
    return new Linter(parser, linterVisitor);
  }

  @Override
  public float getProgress() {
    return 0;
  }

  @Override
  public void notifyObservers() {}

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
