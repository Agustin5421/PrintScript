package interpreter.visitor.factory;

import ast.root.AstNodeType;
import interpreter.ValueCollector;
import interpreter.visitor.InterpreterVisitorV3;
import interpreter.visitor.repository.VariablesRepository;
import interpreter.visitor.strategy.StrategyContainer;
import interpreter.visitor.strategy.callexpression.*;
import interpreter.visitor.strategy.literal.*;
import output.OutputResult;

public class InterpreterVisitorV3Factory {
  private static final MainVisitorStrategyFactory mainVisitorStrategyFactory =
      new MainVisitorStrategyFactory();
  private static final ValueCollectorStrategyFactory valueCollectorStrategyFactory =
      new ValueCollectorStrategyFactory();

  public static InterpreterVisitorV3 getVisitor(String version, OutputResult<String> outputResult) {
    StrategyContainer<AstNodeType> visitorStrategies =
        mainVisitorStrategyFactory.createStrategyContainer();

    StrategyContainer<AstNodeType> collectorStrategies =
        switch (version) {
          case "1.0" -> valueCollectorStrategyFactory.createStrategyContainerV1();
          case "1.1" -> valueCollectorStrategyFactory.createStrategyContainerV2();
          default -> throw new IllegalArgumentException("Invalid version: " + version);
        };

    ValueCollector valueCollector = new ValueCollector(collectorStrategies);

    VariablesRepository varRepo = new VariablesRepository();
    return new InterpreterVisitorV3(varRepo, visitorStrategies, outputResult, valueCollector);
  }
}
