package formatter;

import formatter.visitor.FormatterVisitor;
import java.util.Iterator;
import parsers.Parser;

public class MainFormatter implements Iterator<String> {
  private FormatterVisitor visitor;
  private final Parser parser;

  public MainFormatter(FormatterVisitor visitor, Parser parser) {
    this.visitor = visitor;
    this.parser = parser;
  }

  public String formatProgram() {
    StringBuilder formattedCode = new StringBuilder();
    while (hasNext()) {
      formattedCode.append(next());
    }
    return formattedCode.toString();
  }

  @Override
  public boolean hasNext() {
    return parser.hasNext();
  }

  @Override
  public String next() {
    visitor = (FormatterVisitor) parser.next().accept(visitor);
    return visitor.getCurrentCode();
  }
}
