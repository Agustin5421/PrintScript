package interpreter.visitor.factory;

import ast.root.AstNodeType;
import interpreter.ValueCollector;
import interpreter.visitor.InterpreterVisitorV3;
import interpreter.visitor.repository.VariablesRepository;
import interpreter.visitor.strategy.StrategyContainer;
import output.OutputResult;

public class InterpreterVisitorV3Factory {
  private static final MainVisitorStrategyFactory mainVisitorStrategyFactory =
      new MainVisitorStrategyFactory();
  private static final ValueCollectorStrategyFactory valueCollectorStrategyFactory =
      new ValueCollectorStrategyFactory();

  public static InterpreterVisitorV3 getVisitor(String version, OutputResult<String> outputResult) {
    StrategyContainer<AstNodeType> visitorStrategies;
    StrategyContainer<AstNodeType> collectorStrategies;

    switch (version) {
      case "1.0" -> {
        visitorStrategies = mainVisitorStrategyFactory.createStrategyContainerV1();
        collectorStrategies = valueCollectorStrategyFactory.createStrategyContainerV1();
      }
      case "1.1" -> {
        visitorStrategies = mainVisitorStrategyFactory.createStrategyContainerV2();
        collectorStrategies = valueCollectorStrategyFactory.createStrategyContainerV2();
      }
      default -> throw new IllegalArgumentException("Invalid version: " + version);
    }

    ValueCollector valueCollector = new ValueCollector(collectorStrategies);

    VariablesRepository varRepo = new VariablesRepository();
    return new InterpreterVisitorV3(varRepo, visitorStrategies, outputResult, valueCollector);
  }
}
