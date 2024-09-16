package interpreter.visitor.factory;

import ast.root.AstNodeType;
import interpreter.visitor.strategy.InterpretingStrategy;
import interpreter.visitor.strategy.StrategyContainer;
import interpreter.visitor.strategy.assignment.AssignmentExpressionStrategy;
import interpreter.visitor.strategy.callexpression.CallExpressionStrategy;
import interpreter.visitor.strategy.callexpression.PrintingStrategy;
import interpreter.visitor.strategy.callexpression.PrintlnStrategy;
import interpreter.visitor.strategy.vardec.VariableDeclarationStrategy;
import java.util.Map;

public class MainVisitorStrategyFactory {
  public StrategyContainer<AstNodeType> createStrategyContainer() {
    InterpretingStrategy varDecStrategy = new VariableDeclarationStrategy();
    InterpretingStrategy assignStrategy = new AssignmentExpressionStrategy();
    InterpretingStrategy callExpStrategy = getCallExpStrategy();

    Map<AstNodeType, InterpretingStrategy> visitorStratMap =
        Map.of(
            AstNodeType.VARIABLE_DECLARATION, varDecStrategy,
            AstNodeType.ASSIGNMENT_EXPRESSION, assignStrategy,
            AstNodeType.CALL_EXPRESSION, callExpStrategy);

    return new StrategyContainer<>(visitorStratMap);
  }

  private InterpretingStrategy getCallExpStrategy() {
    PrintingStrategy printingStrategy = new PrintingStrategy();
    InterpretingStrategy printlnStrategy = new PrintlnStrategy(printingStrategy);
    Map<String, InterpretingStrategy> callExpStratMap = Map.of("println", printlnStrategy);
    StrategyContainer<String> callExpStrategies = new StrategyContainer<>(callExpStratMap);

    return new CallExpressionStrategy(callExpStrategies);
  }
}
