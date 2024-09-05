package formatter.strategy.factory;

import com.google.gson.JsonObject;
import formatter.strategy.FormattingStrategy;
import formatter.strategy.common.OperatorConcatenationStrategy;
import formatter.strategy.reassign.ReAssignationStrategy;

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
