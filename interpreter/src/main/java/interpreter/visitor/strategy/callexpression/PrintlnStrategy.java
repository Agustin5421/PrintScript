package interpreter.visitor.strategy.callexpression;

import ast.literal.Literal;
import ast.root.AstNode;
import ast.statements.CallExpression;
import ast.visitor.NodeVisitor;
import interpreter.visitor.InterpreterVisitorV3;
import interpreter.visitor.ValueCollector;
import interpreter.visitor.strategy.InterpretingStrategy;

public class PrintlnStrategy implements InterpretingStrategy {
  private final PrintingStrategy printingStrategy;

  public PrintlnStrategy(PrintingStrategy printingStrategy) {
    this.printingStrategy = printingStrategy;
  }

  @Override
  public NodeVisitor interpret(AstNode node, NodeVisitor visitor) {
    CallExpression callExp = (CallExpression) node;

    ValueCollector valueCollector = ((InterpreterVisitorV3) visitor).getValueCollector();
    AstNode argumentToPrint = callExp.arguments().get(0);
    Literal<?> literal = ((ValueCollector) valueCollector.visit(argumentToPrint)).getValue();

    printingStrategy.interpret(literal, visitor);
    return visitor;
  }
}
