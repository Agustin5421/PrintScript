package interpreter.visitor.factory;

import ast.root.AstNodeType;
import interpreter.visitor.strategy.InterpretingStrategy;
import interpreter.visitor.strategy.binary.*;
import interpreter.visitor.strategy.callexpression.*;
import interpreter.visitor.strategy.identifier.IdentifierStrategy;
import interpreter.visitor.strategy.literal.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import strategy.StrategyContainer;

public class ValueCollectorStrategyFactory {
  public StrategyContainer<AstNodeType, InterpretingStrategy> createStrategyContainerV1() {
    Map<AstNodeType, InterpretingStrategy> commonStrategies = createCommonStrategiesMapV1();

    ArrayList<BinaryProcedure> procedures = getBinaryProcedures();
    BinaryExpressionStrategy binaryExpressionStrategy = new BinaryExpressionStrategy(procedures);

    commonStrategies.put(AstNodeType.BINARY_EXPRESSION, binaryExpressionStrategy);

    return new StrategyContainer<>(commonStrategies, "Can't interpret ");
  }

  public StrategyContainer<AstNodeType, InterpretingStrategy> createStrategyContainerV2() {
    Map<AstNodeType, InterpretingStrategy> commonStrategies = createCommonStrategiesMapV2();

    InterpretingStrategy innerCallExpStrategy = getCallExpStrategy();

    commonStrategies.put(AstNodeType.CALL_EXPRESSION, innerCallExpStrategy);

    ArrayList<BinaryProcedure> procedures = getBinaryProcedures();
    BinaryProcedure booleanProcedure = new BooleanFailingProcedure();
    procedures.add(booleanProcedure);
    BinaryExpressionStrategy binaryExpressionStrategy = new BinaryExpressionStrategy(procedures);

    commonStrategies.put(AstNodeType.BINARY_EXPRESSION, binaryExpressionStrategy);

    return new StrategyContainer<>(commonStrategies, "Can't interpret ");
  }

  private ArrayList<BinaryProcedure> getBinaryProcedures() {
    BinaryProcedure stringProcedure = new StringOperationProcedure();
    BinaryProcedure numberProcedure = new NumberOperationProcedure();
    return new ArrayList<>(List.of(stringProcedure, numberProcedure));
  }

  private Map<AstNodeType, InterpretingStrategy> createCommonStrategiesMapV1() {
    InterpretingStrategy identifierStrategy = new IdentifierStrategy();
    InterpretingStrategy literalStrategy = new LiteralStrategy();

    return new HashMap<>(
        Map.of(
            AstNodeType.IDENTIFIER, identifierStrategy,
            AstNodeType.STRING_LITERAL, literalStrategy,
            AstNodeType.NUMBER_LITERAL, literalStrategy));
  }

  private Map<AstNodeType, InterpretingStrategy> createCommonStrategiesMapV2() {
    Map<AstNodeType, InterpretingStrategy> commonStrategies = createCommonStrategiesMapV1();
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

    StrategyContainer<String, InterpretingStrategy> innerCallExpStrategies =
        new StrategyContainer<>(innerCallExpStratMap, "Can't interpret ");

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
