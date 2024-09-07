package linter;

import ast.root.AstNode;
import java.util.Iterator;
import linter.report.FullReport;
import linter.visitor.LinterVisitor;
import observers.Progressable;
import parsers.Parser;

public class IterableLinter implements Progressable, Iterator<FullReport> {
  private final Parser parser;
  private final LinterVisitor linterVisitor;

  public IterableLinter(Parser parser, LinterVisitor linterVisitor) {
    this.parser = parser;
    this.linterVisitor = linterVisitor;
  }

  public FullReport lint(AstNode astNode) {
    LinterVisitor visitor = (LinterVisitor) astNode.accept(linterVisitor);
    return visitor.getFullReport();
  }

  public IterableLinter setParser(Parser parser) {
    return new IterableLinter(parser, linterVisitor);
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
