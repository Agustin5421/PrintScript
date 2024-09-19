package interpreter.engine;

import ast.root.AstNode;
import ast.root.AstNodeType;
import interpreter.engine.repository.VariablesRepository;
import interpreter.engine.strategy.statement.StatementStrategy;
import output.OutputResult;
import strategy.StrategyContainer;

public class InterpreterEngine {
  private final VariablesRepository variablesRepository;
  // Only works with VariableDeclaration, AssignmentExpression and CallExpression (only println()
  // method).
  private final StrategyContainer<AstNodeType, StatementStrategy> strategies;
  private final OutputResult<String> outputResult;
  private final ValueCollector valueCollector;

  public InterpreterEngine(
      VariablesRepository variablesRepository,
      StrategyContainer<AstNodeType, StatementStrategy> strategies,
      OutputResult<String> outputResult,
      ValueCollector valueCollector) {
    this.variablesRepository = variablesRepository;
    this.strategies = strategies;
    this.outputResult = outputResult;
    this.valueCollector = valueCollector;
  }

  public InterpreterEngine setVariablesRepository(VariablesRepository repository) {
    return new InterpreterEngine(repository, strategies, outputResult, valueCollector);
  }

  public VariablesRepository getVariablesRepository() {
    return variablesRepository;
  }

  public OutputResult<String> getOutputResult() {
    return outputResult;
  }

  public ValueCollector getValueCollector() {
    return valueCollector.setVariablesRepository(variablesRepository);
  }

  public InterpreterEngine interpret(AstNode node) {
    AstNodeType nodeType = node.getNodeType();
    StatementStrategy strategyToApply = strategies.getStrategy(nodeType);

    if (strategyToApply == null) {
      throw new IllegalArgumentException("No strategy found for node type: " + nodeType);
    }

    return strategyToApply.apply(node, this);
  }
}
