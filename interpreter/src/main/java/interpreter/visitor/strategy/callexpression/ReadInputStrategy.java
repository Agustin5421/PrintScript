package interpreter.visitor.strategy.callexpression;

import ast.literal.Literal;
import ast.root.AstNode;
import ast.statements.CallExpression;
import ast.visitor.NodeVisitor;
import interpreter.visitor.ValueCollector;
import interpreter.visitor.staticprovider.Inputs;
import interpreter.visitor.strategy.InterpretingStrategy;

public class ReadInputStrategy implements InterpretingStrategy {
  private final LiteralHandler literalHandler;
  private final PrintingStrategy printingStrategy;

  public ReadInputStrategy(LiteralHandler literalHandler, PrintingStrategy printingStrategy) {
    this.literalHandler = literalHandler;
    this.printingStrategy = printingStrategy;
  }

  @Override
  public NodeVisitor apply(AstNode node, NodeVisitor visitor) {
    // We receive a collector to store the value of the literal
    ValueCollector valueCollector = (ValueCollector) visitor;
    CallExpression callExpression = (CallExpression) node;

    // Create a literal from the user input
    String input = Inputs.nextInput();
    Literal<?> processedLiteral = literalHandler.getLiteral(callExpression, input);

    // saves the literal in the output result
    Literal<?> argumentLiteral = (Literal<?>) callExpression.arguments().get(0);
    printingStrategy.apply(argumentLiteral, valueCollector);

    // return the collector with the value of the literal
    return valueCollector.setValue(processedLiteral);
  }

  /*
  private NodeVisitor handleReadInput(CallExpression callExpression) {
      String savedInputs = Inputs.nextInput().poll();
      // Check if there is a user input saved in the queue or else it will
      // wait for the user to input a value
      String userInput = savedInputs == null ? "" : savedInputs;
      ResultLiteral result = literalHandler.handleReadInput(callExpression, printedValues, userInput);
      Literal<?> resultLiteral = result.literal();
      List<String> newPrintedValues = result.strings();

      Identifier identifier = callExpression.methodIdentifier();
      VariablesRepository newVariablesRepository =
              variablesRepository.addVariable(identifier, resultLiteral);
      return new InterpreterVisitorV2(
              previousVisitor, newVariablesRepository, newPrintedValues, resultLiteral);
  }

   */
}
