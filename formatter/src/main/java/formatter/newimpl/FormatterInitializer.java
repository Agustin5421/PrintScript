package formatter.newimpl;

import ast.root.AstNodeType;
import com.google.gson.JsonObject;
import formatter.OptionsChecker;
import formatter.newimpl.strategy.*;
import formatter.newimpl.strategy.factory.CallExpressionFactory;
import formatter.newimpl.strategy.factory.FormattingStrategyFactory;
import formatter.newimpl.strategy.factory.ReAssignmentFactory;
import formatter.newimpl.strategy.factory.VariableDeclarationStrategyFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import observers.Observer;
import observers.ProgressObserver;
import observers.ProgressPrinter;

public class FormatterInitializer {
  public static MainFormatter2 init(String options) {
    return init(new ProgressObserver(new ProgressPrinter(), 1), options);
  }

  public static MainFormatter2 init(Observer observer, String options) {
    JsonObject rules = OptionsChecker.checkAndReturn(options);
    return new MainFormatter2(List.of(observer), createVisitor(rules));
  }

  private static FormatterVisitor2 createVisitor(JsonObject rules) {
    Map<AstNodeType, FormattingStrategy> strategies = new HashMap<>();

    FormattingStrategyFactory callExpressionFactory = new CallExpressionFactory();
    FormattingStrategy callExpressionStrategy = callExpressionFactory.create(rules);
    strategies.put(AstNodeType.CALL_EXPRESSION, callExpressionStrategy);

    OperatorConcatenationStrategy assignmentStrategy = getAssignmentStrategy(rules);

    FormattingStrategyFactory reAssignmentFactory = new ReAssignmentFactory(assignmentStrategy);
    FormattingStrategy reAssignmentStrategy = reAssignmentFactory.create(rules);
    strategies.put(AstNodeType.ASSIGNMENT_EXPRESSION, reAssignmentStrategy);

    FormattingStrategyFactory varDecStrategyFactory =
        new VariableDeclarationStrategyFactory(assignmentStrategy);
    FormattingStrategy varDecStrategy = varDecStrategyFactory.create(rules);
    strategies.put(AstNodeType.VARIABLE_DECLARATION, varDecStrategy);

    return new FormatterVisitor2(strategies);
  }

  private static OperatorConcatenationStrategy getAssignmentStrategy(JsonObject rules) {
    boolean equalSpace = rules.get("equalSpaces").getAsBoolean();
    List<FormattingStrategy> strategies = new ArrayList<>();
    OperatorStrategy operatorStrategy = new OperatorStrategy("=");
    if (equalSpace) {
      WhiteSpace whiteSpace = new WhiteSpace();
      strategies.add(whiteSpace);
      strategies.add(operatorStrategy);
      strategies.add(whiteSpace);
    } else {
      strategies.add(operatorStrategy);
    }
    return new OperatorConcatenationStrategy(strategies);
  }
}
