package interpreter.visitor.strategy.callexpression;

import ast.root.AstNode;
import ast.statements.CallExpression;
import ast.visitor.NodeVisitor;
import interpreter.visitor.strategy.InterpretingStrategy;

public class PrintlnStrategy implements InterpretingStrategy {
  private final PrintingStrategy printingStrategy;

  public PrintlnStrategy(PrintingStrategy printingStrategy) {
    this.printingStrategy = printingStrategy;
  }

  @Override
  public NodeVisitor interpret(AstNode node, NodeVisitor visitor) {
    CallExpression callExp = (CallExpression) node;
    printingStrategy.interpret(callExp.arguments().get(0), visitor);
    return visitor;
  }
}
