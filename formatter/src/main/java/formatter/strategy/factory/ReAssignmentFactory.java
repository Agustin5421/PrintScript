package formatter.strategy.factory;

import com.google.gson.JsonObject;
import formatter.strategy.FormattingStrategy;
import formatter.strategy.common.AssignationStrategy;
import formatter.strategy.reassign.ReAssignationStrategy;

public class ReAssignmentFactory implements FormattingStrategyFactory {
  private final AssignationStrategy equalStrategy;

  public ReAssignmentFactory(AssignationStrategy equalStrategy) {
    this.equalStrategy = equalStrategy;
  }

  @Override
  public FormattingStrategy create(JsonObject rules, String version) {
    return new ReAssignationStrategy(equalStrategy);
  }
}
