package interpreter.engine.strategy.statement.callexpression;

import ast.literal.Literal;
import ast.root.AstNode;
import ast.statements.CallExpression;
import interpreter.engine.InterpreterEngine;
import interpreter.engine.ValueCollector;
import interpreter.engine.strategy.Printer;
import interpreter.engine.strategy.statement.StatementStrategy;

public class PrintlnStrategy implements StatementStrategy {
  private final Printer printer;

  public PrintlnStrategy(Printer printer) {
    this.printer = printer;
  }

  @Override
  public InterpreterEngine apply(AstNode node, InterpreterEngine engine) {
    CallExpression callExp = (CallExpression) node;

    ValueCollector valueCollector = engine.getValueCollector();
    AstNode argumentToPrint = callExp.arguments().get(0);
    Literal<?> literal = valueCollector.evaluate(argumentToPrint).getValue();

    printer.apply(literal, engine.getOutputResult());
    return engine;
  }
}
