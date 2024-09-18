package interpreter.visitor;

import ast.root.AstNode;
import ast.root.AstNodeType;
import ast.visitor.NodeVisitor;
import interpreter.visitor.repository.VariablesRepository;
import interpreter.visitor.strategy.InterpretingStrategy;
import output.OutputResult;
import strategy.StrategyContainer;

public class InterpreterVisitorV3 implements OutputVisitor {
  private final VariablesRepository variablesRepository;
  // Only works with VariableDeclaration, AssignmentExpression and CallExpression (only println()
  // method).
  private final StrategyContainer<AstNodeType, InterpretingStrategy> strategies;
  private final OutputResult<String> outputResult;
  private final ValueCollector valueCollector;

  public InterpreterVisitorV3(
      VariablesRepository variablesRepository,
      StrategyContainer<AstNodeType, InterpretingStrategy> strategies,
      OutputResult<String> outputResult,
      ValueCollector valueCollector) {
    this.variablesRepository = variablesRepository;
    this.strategies = strategies;
    this.outputResult = outputResult;
    this.valueCollector = valueCollector;
  }

  @Override
  public OutputVisitor setVariablesRepository(VariablesRepository repository) {
    return new InterpreterVisitorV3(repository, strategies, outputResult, valueCollector);
  }

  @Override
  public VariablesRepository getVariablesRepository() {
    return variablesRepository;
  }

  @Override
  public OutputResult<String> getOutputResult() {
    return outputResult;
  }

  public StrategyContainer<AstNodeType, InterpretingStrategy> getStrategies() {
    return strategies;
  }

  public ValueCollector getValueCollector() {
    return valueCollector.setVariablesRepository(variablesRepository);
  }

  @Override
  public NodeVisitor visit(AstNode node) {
    AstNodeType nodeType = node.getNodeType();
    return strategies.getStrategy(nodeType).apply(node, this);
  }

  public InterpreterVisitorV3 cloneVisitor() {
    return new InterpreterVisitorV3(variablesRepository, strategies, outputResult, valueCollector);
  }
}
