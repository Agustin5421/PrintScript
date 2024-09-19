package interpreter.engine.factory;

import ast.root.AstNodeType;
import interpreter.engine.strategy.Printer;
import interpreter.engine.strategy.expression.ExpressionStrategy;
import interpreter.engine.strategy.expression.binary.*;
import interpreter.engine.strategy.expression.callexpression.CallExpressionStrategy;
import interpreter.engine.strategy.expression.callexpression.ReadEnvStrategy;
import interpreter.engine.strategy.expression.callexpression.ReadInputStrategy;
import interpreter.engine.strategy.expression.identifier.IdentifierStrategy;
import interpreter.engine.strategy.expression.literal.*;
import interpreter.engine.strategy.expression.literal.LiteralHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import strategy.StrategyContainer;

public class ExpressionStrategiesFactory {
  public StrategyContainer<AstNodeType, ExpressionStrategy> createStrategyContainerV1() {
    Map<AstNodeType, ExpressionStrategy> commonStrategies = createCommonStrategiesMapV1();

    ArrayList<BinaryProcedure> procedures = getBinaryProcedures();
    BinaryExpressionStrategy binaryExpressionStrategy = new BinaryExpressionStrategy(procedures);

    commonStrategies.put(AstNodeType.BINARY_EXPRESSION, binaryExpressionStrategy);

    return new StrategyContainer<>(commonStrategies, "Can't interpret ");
  }

  public StrategyContainer<AstNodeType, ExpressionStrategy> createStrategyContainerV2() {
    Map<AstNodeType, ExpressionStrategy> commonStrategies = createCommonStrategiesMapV2();

    ExpressionStrategy innerCallExpStrategy = getCallExpStrategy();

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

  private Map<AstNodeType, ExpressionStrategy> createCommonStrategiesMapV1() {
    ExpressionStrategy identifierStrategy = new IdentifierStrategy();
    ExpressionStrategy literalStrategy = new LiteralStrategy();

    return new HashMap<>(
        Map.of(
            AstNodeType.IDENTIFIER, identifierStrategy,
            AstNodeType.STRING_LITERAL, literalStrategy,
            AstNodeType.NUMBER_LITERAL, literalStrategy));
  }

  private Map<AstNodeType, ExpressionStrategy> createCommonStrategiesMapV2() {
    Map<AstNodeType, ExpressionStrategy> commonStrategies = createCommonStrategiesMapV1();
    ExpressionStrategy literalStrategy = new LiteralStrategy();

    commonStrategies.put(AstNodeType.BOOLEAN_LITERAL, literalStrategy);

    return commonStrategies;
  }

  private ExpressionStrategy getCallExpStrategy() {
    LiteralHandler literalHandler = createLiteralHandler();

    Printer printer = new Printer();
    ExpressionStrategy readInputStrategy = new ReadInputStrategy(literalHandler, printer);
    ExpressionStrategy readEnvStrategy = new ReadEnvStrategy(literalHandler);

    Map<String, ExpressionStrategy> innerCallExpStratMap =
        Map.of(
            "readInput", readInputStrategy,
            "readEnv", readEnvStrategy);

    StrategyContainer<String, ExpressionStrategy> innerCallExpStrategies =
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
