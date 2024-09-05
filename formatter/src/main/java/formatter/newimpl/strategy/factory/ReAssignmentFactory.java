package formatter.newimpl.strategy.factory;

import com.google.gson.JsonObject;
import formatter.newimpl.strategy.FormattingStrategy;
import formatter.newimpl.strategy.OperatorConcatenationStrategy;
import formatter.newimpl.strategy.ReAssignationStrategy;

public class ReAssignmentFactory implements FormattingStrategyFactory {
  private final OperatorConcatenationStrategy equalStrategy;

  public ReAssignmentFactory(OperatorConcatenationStrategy equalStrategy) {
    this.equalStrategy = equalStrategy;
  }

  @Override
  public FormattingStrategy create(JsonObject rules) {
    return new ReAssignationStrategy(equalStrategy);
  }
}
