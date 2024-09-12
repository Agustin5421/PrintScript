package formatter.strategy.ifelse;

import ast.identifier.Identifier;
import ast.literal.BooleanLiteral;
import ast.root.AstNode;
import formatter.strategy.FormattingStrategy;
import formatter.strategy.common.CallStrategy;
import formatter.visitor.FormatterVisitor;
import java.util.List;

public class ConditionalStatementStrategy implements FormattingStrategy {
  private final String keyword;
  private final String condition;
  // Strategy for adding the conditional statement
  private final CallStrategy callStrategy;

  public ConditionalStatementStrategy(CallStrategy callStrategy, String keyword, String condition) {
    this.keyword = keyword;
    this.condition = condition;
    this.callStrategy = callStrategy;
  }

  public ConditionalStatementStrategy(CallStrategy callStrategy) {
    this(callStrategy, "", "");
  }

  @Override
  public String apply(AstNode node, FormatterVisitor visitor) {
    AstNode conditionValue;

    if (condition.equals("true") || condition.equals("false")) {
      conditionValue = new BooleanLiteral(Boolean.valueOf(condition), null, null);
    } else {
      conditionValue = new Identifier(condition, null, null);
    }

    CallStrategy newCallStrategy = callStrategy.newStrategy(keyword, List.of(conditionValue));
    return newCallStrategy.apply(node, visitor);
  }

  public ConditionalStatementStrategy newStrategy(String keyword, String condition) {
    return new ConditionalStatementStrategy(callStrategy, keyword, condition);
  }
}
