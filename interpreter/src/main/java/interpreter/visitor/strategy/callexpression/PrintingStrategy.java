package interpreter.visitor.strategy.callexpression;

import ast.literal.Literal;
import ast.root.AstNode;
import ast.visitor.NodeVisitor;
import interpreter.visitor.OutputVisitor;
import interpreter.visitor.strategy.InterpretingStrategy;
import output.OutputResult;

public class PrintingStrategy implements InterpretingStrategy {
  @Override
  public NodeVisitor interpret(AstNode node, NodeVisitor visitor) {
    Literal<?> literal = (Literal<?>) node;
    OutputVisitor outputVisitor = (OutputVisitor) visitor;
    OutputResult<String> outputResult = outputVisitor.getOutputResult();
    outputResult.saveResult(literal.value().toString());
    return visitor;
  }
}
