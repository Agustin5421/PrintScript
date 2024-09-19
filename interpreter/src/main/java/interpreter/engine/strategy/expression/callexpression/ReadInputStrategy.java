package interpreter.engine.strategy.expression.callexpression;

import ast.literal.Literal;
import ast.root.AstNode;
import ast.statements.CallExpression;
import interpreter.engine.ValueCollector;
import interpreter.engine.staticprovider.Inputs;
import interpreter.engine.strategy.Printer;
import interpreter.engine.strategy.expression.ExpressionStrategy;
import interpreter.engine.strategy.expression.literal.LiteralHandler;

public class ReadInputStrategy implements ExpressionStrategy {
  private final LiteralHandler literalHandler;
  private final Printer printer;

  public ReadInputStrategy(LiteralHandler literalHandler, Printer printer) {
    this.literalHandler = literalHandler;
    this.printer = printer;
  }

  @Override
  public ValueCollector apply(AstNode node, ValueCollector engine) {
    // We receive a collector to store the value of the literal
    CallExpression callExpression = (CallExpression) node;

    // Create a literal from the user input
    String input = Inputs.nextInput();
    Literal<?> processedLiteral = literalHandler.getLiteral(callExpression, input);

    // saves the literal in the output result
    Literal<?> argumentLiteral = (Literal<?>) callExpression.arguments().get(0);
    printer.apply(argumentLiteral, engine.getOutputResult());

    // return the collector with the value of the literal
    return engine.setValue(processedLiteral);
  }
}
