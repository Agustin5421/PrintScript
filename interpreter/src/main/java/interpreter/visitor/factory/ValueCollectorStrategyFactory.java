package interpreter.visitor.factory;

import ast.root.AstNodeType;
import interpreter.visitor.strategy.InterpretingStrategy;
import interpreter.visitor.strategy.StrategyContainer;
import interpreter.visitor.strategy.callexpression.*;
import interpreter.visitor.strategy.identifier.IdentifierStrategy;
import interpreter.visitor.strategy.literal.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ValueCollectorStrategyFactory {
  public StrategyContainer<AstNodeType> createStrategyContainerV1() {
    Map<AstNodeType, InterpretingStrategy> commonStrategies = createCommonStrategiesMap();

    return new StrategyContainer<>(commonStrategies);
  }

  public StrategyContainer<AstNodeType> createStrategyContainerV2() {
    Map<AstNodeType, InterpretingStrategy> commonStrategies = createCommonStrategiesMapV2();

    InterpretingStrategy innerCallExpStrategy = getCallExpStrategy();

    commonStrategies.put(AstNodeType.CALL_EXPRESSION, innerCallExpStrategy);

    return new StrategyContainer<>(commonStrategies);
  }

  private Map<AstNodeType, InterpretingStrategy> createCommonStrategiesMap() {
    InterpretingStrategy identifierStrategy = new IdentifierStrategy();
    InterpretingStrategy literalStrategy = new LiteralStrategy();
    // Add BinaryExpressionStrategy when implemented

    return new HashMap<>(
        Map.of(
            AstNodeType.IDENTIFIER, identifierStrategy,
            AstNodeType.STRING_LITERAL, literalStrategy,
            AstNodeType.NUMBER_LITERAL, literalStrategy));
  }

  private Map<AstNodeType, InterpretingStrategy> createCommonStrategiesMapV2() {
    Map<AstNodeType, InterpretingStrategy> commonStrategies = createCommonStrategiesMap();
    InterpretingStrategy literalStrategy = new LiteralStrategy();

    commonStrategies.put(AstNodeType.BOOLEAN_LITERAL, literalStrategy);

    return commonStrategies;
  }

  private InterpretingStrategy getCallExpStrategy() {
    LiteralHandler literalHandler = createLiteralHandler();

    PrintingStrategy printingStrategy = new PrintingStrategy();
    InterpretingStrategy readInputStrategy =
        new ReadInputStrategy(literalHandler, printingStrategy);
    InterpretingStrategy readEnvStrategy = new ReadEnvStrategy(literalHandler);

    Map<String, InterpretingStrategy> innerCallExpStratMap =
        Map.of(
            "readInput", readInputStrategy,
            "readEnv", readEnvStrategy);

    StrategyContainer<String> innerCallExpStrategies =
        new StrategyContainer<>(innerCallExpStratMap);

    return new CallExpressionStrategy(innerCallExpStrategies);
  }

  private LiteralHandler createLiteralHandler() {
    LiteralFactory stringFactory = new StringLiteralFactory();
    LiteralFactory numberFactory = new NumberLiteralFactory();
    LiteralFactory booleanFactory = new BooleanLiteralFactory();

    Map<String, LiteralFactory> factoryMap = new LinkedHashMap<>();
    factoryMap.put("(?i)^(true|false|t|f)$", booleanFactory);
    factoryMap.put("-?\\d+(\\.\\d+)?([eE]-?\\d+)?", numberFactory);
    factoryMap.put(".*", stringFactory);

    return new LiteralHandler(factoryMap);
  }
}
