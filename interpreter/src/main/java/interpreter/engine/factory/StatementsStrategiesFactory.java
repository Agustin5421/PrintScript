package interpreter.engine.factory;

import ast.root.AstNodeType;
import interpreter.engine.strategy.Printer;
import interpreter.engine.strategy.statement.StatementStrategy;
import interpreter.engine.strategy.statement.assignment.AssignmentExpressionStrategy;
import interpreter.engine.strategy.statement.callexpression.CallExpressionStrategy;
import interpreter.engine.strategy.statement.callexpression.PrintlnStrategy;
import interpreter.engine.strategy.statement.conditional.IfStrategy;
import interpreter.engine.strategy.statement.vardec.VariableDeclarationStrategy;
import java.util.HashMap;
import java.util.Map;
import strategy.StrategyContainer;

public class StatementsStrategiesFactory {
  public StrategyContainer<AstNodeType, StatementStrategy> createStrategyContainerV1() {
    Map<AstNodeType, StatementStrategy> visitorStratMap = getCommonStrategies();

    return new StrategyContainer<>(visitorStratMap, "Can't interpret ");
  }

  public StrategyContainer<AstNodeType, StatementStrategy> createStrategyContainerV2() {
    Map<AstNodeType, StatementStrategy> visitorStratMap = getCommonStrategies();

    StatementStrategy ifStatementStrategy = new IfStrategy();

    visitorStratMap.put(AstNodeType.IF_STATEMENT, ifStatementStrategy);

    return new StrategyContainer<>(visitorStratMap, "Can't interpret ");
  }

  private Map<AstNodeType, StatementStrategy> getCommonStrategies() {
    StatementStrategy varDecStrategy = new VariableDeclarationStrategy();
    StatementStrategy assignStrategy = new AssignmentExpressionStrategy();
    StatementStrategy callExpStrategy = getCallExpStrategy();

    return new HashMap<>(
        Map.of(
            AstNodeType.VARIABLE_DECLARATION, varDecStrategy,
            AstNodeType.ASSIGNMENT_EXPRESSION, assignStrategy,
            AstNodeType.CALL_EXPRESSION, callExpStrategy));
  }

  private StatementStrategy getCallExpStrategy() {
    Printer printer = new Printer();
    StatementStrategy printlnStrategy = new PrintlnStrategy(printer);
    Map<String, StatementStrategy> callExpStratMap = Map.of("println", printlnStrategy);
    StrategyContainer<String, StatementStrategy> callExpStrategies =
        new StrategyContainer<>(callExpStratMap, "Can't interpret ");

    return new CallExpressionStrategy(callExpStrategies);
  }
}
