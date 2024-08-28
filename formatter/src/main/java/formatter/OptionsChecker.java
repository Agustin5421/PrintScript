package formatter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class OptionsChecker {
  public JsonObject checkAndReturn(String options) throws JsonSyntaxException {
    try {
      JsonObject jsonOptions = JsonParser.parseString(options).getAsJsonObject();
      JsonObject rules = jsonOptions.getAsJsonObject("rules");
      JsonObject colonRules = rules.getAsJsonObject("colonRules");
      colonRules.get("before").getAsBoolean();
      colonRules.get("after").getAsBoolean();
      rules.get("equalSpaces").getAsBoolean();
      rules.get("printLineBreaks").getAsInt();
      return rules;
    } catch (Exception e) {
      throw new JsonSyntaxException("Invalid formatting options");
    }
  }
}
