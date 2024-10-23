package interpreter.engine;

import ast.literal.Literal;
import ast.root.AstNode;
import ast.root.AstNodeType;
import interpreter.engine.repository.VariablesRepository;
import interpreter.engine.strategy.expression.ExpressionStrategy;
import output.OutputResult;
import strategy.StrategyContainer;

public class ValueCollector {
  private final Literal<?> value;
  // Only works with Identifier, Literals, BinaryExpressions and CallExpression (readInput() and
  // readEnv()).
  private final StrategyContainer<AstNodeType, ExpressionStrategy> strategies;
  private final VariablesRepository variablesRepository;
  private final OutputResult<String> outputResult;

  public ValueCollector(
      StrategyContainer<AstNodeType, ExpressionStrategy> strategies,
      Literal<?> value,
      VariablesRepository variablesRepository,
      OutputResult<String> outputResult) {
    this.value = value;
    this.strategies = strategies;
    this.variablesRepository = variablesRepository;
    this.outputResult = outputResult;
  }

  public ValueCollector(
      StrategyContainer<AstNodeType, ExpressionStrategy> strategies,
      OutputResult<String> outputResult) {
    this(strategies, null, null, outputResult);
  }

  public ValueCollector setVariablesRepository(VariablesRepository variablesRepository) {
    return new ValueCollector(strategies, value, variablesRepository, outputResult);
  }

  public VariablesRepository getVariablesRepository() {
    return variablesRepository;
  }

  public ValueCollector setValue(Literal<?> value) {
    return new ValueCollector(strategies, value, variablesRepository, outputResult);
  }

  public Literal<?> getValue() {
    return value;
  }

  public ValueCollector evaluate(AstNode node) {
    // recibe identifier -> pide valor al repositorio y se lo queda
    // recibe literal -> guarda valor
    // recibe binExp -> calcula valor bajando por el arbol
    // recibe callExp -> ejecuta metodo y almacena valor
    // vuelve a varDec, assignment, callExp (println()) -> devuelve valor almacenado

    AstNodeType nodeType = node.getNodeType();
    ExpressionStrategy evaluationStrategy = strategies.getStrategy(nodeType);

    if (evaluationStrategy == null) {
      throw new IllegalArgumentException(
          "Can not evaluate node "
              + node.getNodeType()
              + " from "
              + node.start()
              + " to "
              + node.end());
    }

    return evaluationStrategy.apply(node, this);
  }

  public OutputResult<String> getOutputResult() {
    return outputResult;
  }
}
