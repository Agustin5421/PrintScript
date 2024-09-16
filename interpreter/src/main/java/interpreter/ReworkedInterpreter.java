package interpreter;

import ast.root.AstNode;
import interpreter.visitor.OutputVisitor;
import java.io.IOException;
import java.io.InputStream;
import output.OutputResult;
import parsers.Parser;

public class ReworkedInterpreter {
  private final Parser parser;
  private final OutputVisitor visitor;

  public ReworkedInterpreter(Parser parser, OutputVisitor visitor) {
    this.parser = parser;
    this.visitor = visitor;
  }

  public OutputResult<String> interpret(InputStream code) {
    try {
      parser.setLexer(parser.getLexer().setInput(code));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    OutputVisitor visitor = this.visitor;
    while (parser.hasNext()) {
      AstNode statement = parser.next();
      visitor = (OutputVisitor) statement.accept(visitor);
    }

    return visitor.getOutputResult();
  }
}
