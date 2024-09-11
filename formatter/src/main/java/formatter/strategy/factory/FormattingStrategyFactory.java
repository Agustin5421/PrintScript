package formatter.strategy.factory;

import com.google.gson.JsonObject;
import formatter.strategy.FormattingStrategy;

public interface FormattingStrategyFactory {
  FormattingStrategy create(JsonObject rules, String version);
}
