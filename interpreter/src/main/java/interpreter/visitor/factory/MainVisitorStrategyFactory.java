package interpreter.visitor.factory;

import ast.root.AstNodeType;
import interpreter.visitor.strategy.InterpretingStrategy;
import interpreter.visitor.strategy.assignment.AssignmentExpressionStrategy;
import interpreter.visitor.strategy.callexpression.CallExpressionStrategy;
import interpreter.visitor.strategy.callexpression.PrintingStrategy;
import interpreter.visitor.strategy.callexpression.PrintlnStrategy;
import interpreter.visitor.strategy.conditional.IfStrategy;
import interpreter.visitor.strategy.vardec.VariableDeclarationStrategy;
import java.util.HashMap;
import java.util.Map;
import strategy.StrategyContainer;

public class MainVisitorStrategyFactory {
  public StrategyContainer<AstNodeType, InterpretingStrategy> createStrategyContainerV1() {
    Map<AstNodeType, InterpretingStrategy> visitorStratMap = getCommonStrategies();

    return new StrategyContainer<>(visitorStratMap, "Can't interpret ");
  }

  public StrategyContainer<AstNodeType, InterpretingStrategy> createStrategyContainerV2() {
    Map<AstNodeType, InterpretingStrategy> visitorStratMap = getCommonStrategies();

    InterpretingStrategy ifStatementStrategy = new IfStrategy();

    visitorStratMap.put(AstNodeType.IF_STATEMENT, ifStatementStrategy);

    return new StrategyContainer<>(visitorStratMap, "Can't interpret ");
  }

  private Map<AstNodeType, InterpretingStrategy> getCommonStrategies() {
    InterpretingStrategy varDecStrategy = new VariableDeclarationStrategy();
    InterpretingStrategy assignStrategy = new AssignmentExpressionStrategy();
    InterpretingStrategy callExpStrategy = getCallExpStrategy();

    return new HashMap<>(
        Map.of(
            AstNodeType.VARIABLE_DECLARATION, varDecStrategy,
            AstNodeType.ASSIGNMENT_EXPRESSION, assignStrategy,
            AstNodeType.CALL_EXPRESSION, callExpStrategy));
  }

  private InterpretingStrategy getCallExpStrategy() {
    PrintingStrategy printingStrategy = new PrintingStrategy();
    InterpretingStrategy printlnStrategy = new PrintlnStrategy(printingStrategy);
    Map<String, InterpretingStrategy> callExpStratMap = Map.of("println", printlnStrategy);
    StrategyContainer<String, InterpretingStrategy> callExpStrategies =
        new StrategyContainer<>(callExpStratMap, "Can't interpret ");

    return new CallExpressionStrategy(callExpStrategies);
  }
}
