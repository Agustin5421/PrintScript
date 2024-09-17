package interpreter;

import ast.root.AstNode;
import interpreter.visitor.OutputVisitor;

public class ReworkedInterpreter {
  private final OutputVisitor visitor;

  public OutputVisitor getVisitor() {
    return visitor;
  }

  public ReworkedInterpreter(OutputVisitor visitor) {
    this.visitor = visitor;
  }

  public ReworkedInterpreter interpret(AstNode nodeToInterpret) {
    OutputVisitor afterInterpretation = (OutputVisitor) nodeToInterpret.accept(visitor);

    return new ReworkedInterpreter(afterInterpretation);
  }
}
