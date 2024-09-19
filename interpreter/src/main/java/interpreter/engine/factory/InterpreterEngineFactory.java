package interpreter.engine.factory;

import ast.root.AstNodeType;
import interpreter.engine.InterpreterEngine;
import interpreter.engine.ValueCollector;
import interpreter.engine.repository.VariablesRepository;
import interpreter.engine.strategy.expression.ExpressionStrategy;
import interpreter.engine.strategy.statement.StatementStrategy;
import output.OutputResult;
import strategy.StrategyContainer;

public class InterpreterEngineFactory {
  private static final StatementsStrategiesFactory statementsFactory =
      new StatementsStrategiesFactory();
  private static final ExpressionStrategiesFactory expressionFactory =
      new ExpressionStrategiesFactory();

  public static InterpreterEngine getEngine(String version, OutputResult<String> outputResult) {
    StrategyContainer<AstNodeType, StatementStrategy> statementsStrategies;
    StrategyContainer<AstNodeType, ExpressionStrategy> expressionStrategies;

    switch (version) {
      case "1.0" -> {
        statementsStrategies = statementsFactory.createStrategyContainerV1();
        expressionStrategies = expressionFactory.createStrategyContainerV1();
      }
      case "1.1" -> {
        statementsStrategies = statementsFactory.createStrategyContainerV2();
        expressionStrategies = expressionFactory.createStrategyContainerV2();
      }
      default -> throw new IllegalArgumentException("Invalid version: " + version);
    }

    ValueCollector valueCollector = new ValueCollector(expressionStrategies, outputResult);

    VariablesRepository varRepo = new VariablesRepository();
    return new InterpreterEngine(varRepo, statementsStrategies, outputResult, valueCollector);
  }
}
