package formatter.newimpl.strategy.factory;

import com.google.gson.JsonObject;
import formatter.newimpl.strategy.FormattingStrategy;

public interface FormattingStrategyFactory {
  FormattingStrategy create(JsonObject rules);
}
