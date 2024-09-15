package interpreter.visitor;

import ast.root.AstNodeType;
import interpreter.ValueCollector;
import interpreter.visitor.repository.VariablesRepository;
import interpreter.visitor.strategy.InterpretingStrategy;
import interpreter.visitor.strategy.StrategyContainer;
import interpreter.visitor.strategy.assignment.AssignmentExpressionStrategy;
import interpreter.visitor.strategy.callexpression.CallExpressionStrategy;
import interpreter.visitor.strategy.callexpression.PrintingStrategy;
import interpreter.visitor.strategy.callexpression.PrintlnStrategy;
import interpreter.visitor.strategy.identifier.IdentifierStrategy;
import interpreter.visitor.strategy.literal.LiteralStrategy;
import interpreter.visitor.strategy.vardec.VariableDeclarationStrategy;
import java.util.Map;
import output.OutputResult;

public class InterpreterVisitorV3Factory {
  public static InterpreterVisitorV3 getVisitor(String version, OutputResult<String> outputResult) {
    return switch (version) {
      case "1.0" -> buildVisitorV1(outputResult);
      case "1.1" -> buildVisitorV2(outputResult);
      default -> throw new IllegalArgumentException("Invalid version: " + version);
    };
  }

  private static InterpreterVisitorV3 buildVisitorV1(OutputResult<String> outputResult) {
    InterpretingStrategy varDecStrat = new VariableDeclarationStrategy();
    InterpretingStrategy assignStrat = new AssignmentExpressionStrategy();

    PrintingStrategy printingStrat = new PrintingStrategy();
    InterpretingStrategy printlnStrat = new PrintlnStrategy(printingStrat);
    Map<String, InterpretingStrategy> callExpStratMap = Map.of("println", printlnStrat);
    StrategyContainer<String> callExpStrats = new StrategyContainer<>(callExpStratMap);
    InterpretingStrategy callExpStrat = new CallExpressionStrategy(callExpStrats);

    Map<AstNodeType, InterpretingStrategy> visitorStratMap =
        Map.of(
            AstNodeType.VARIABLE_DECLARATION, varDecStrat,
            AstNodeType.ASSIGNMENT_EXPRESSION, assignStrat,
            AstNodeType.CALL_EXPRESSION, callExpStrat);
    StrategyContainer<AstNodeType> visitorStrats = new StrategyContainer<>(visitorStratMap);

    InterpretingStrategy idStrat = new IdentifierStrategy();
    InterpretingStrategy literalStrat = new LiteralStrategy();
    // Add BinaryExpressionStrategy when implemented
    Map<AstNodeType, InterpretingStrategy> valueCollectorStratMap =
        Map.of(
            AstNodeType.IDENTIFIER, idStrat,
            AstNodeType.STRING_LITERAL, literalStrat,
            AstNodeType.NUMBER_LITERAL, literalStrat);
    StrategyContainer<AstNodeType> collectorStrats =
        new StrategyContainer<>(valueCollectorStratMap);
    ValueCollector valueCollector = new ValueCollector(collectorStrats);

    VariablesRepository varRepo = new VariablesRepository();

    return new InterpreterVisitorV3(varRepo, visitorStrats, outputResult, valueCollector);
  }

  private static InterpreterVisitorV3 buildVisitorV2(OutputResult<String> outputResult) {
    return null;
  }
}
