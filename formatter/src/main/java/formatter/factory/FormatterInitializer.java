package formatter.factory;

import ast.root.AstNodeType;
import com.google.gson.JsonObject;
import factory.ParserFactory;
import formatter.MainFormatter;
import formatter.OptionsChecker;
import formatter.strategy.FormattingStrategy;
import formatter.strategy.common.OperatorConcatenationStrategy;
import formatter.strategy.common.OperatorStrategy;
import formatter.strategy.common.WhiteSpace;
import formatter.strategy.factory.CallExpressionFactory;
import formatter.strategy.factory.FormattingStrategyFactory;
import formatter.strategy.factory.ReAssignmentFactory;
import formatter.strategy.factory.VariableDeclarationStrategyFactory;
import formatter.visitor.FormatterVisitor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import observers.Observer;
import observers.ProgressObserver;
import observers.ProgressPrinter;
import parsers.Parser;

public class FormatterInitializer {
  public static MainFormatter init(String options, String code, String version) {
    return init(options, code, version, new ProgressObserver(new ProgressPrinter(), 1));
  }

  public static MainFormatter init(String options, String code, String version, Observer observer) {
    JsonObject rules = OptionsChecker.checkAndReturn(options);
    Parser parser = ParserFactory.getParser(version);
    parser = parser.setLexer(parser.getLexer().setInput(code));
    return new MainFormatter(List.of(observer), createVisitor(rules), parser);
  }

  private static FormatterVisitor createVisitor(JsonObject rules) {
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

    return new FormatterVisitor(strategies);
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
