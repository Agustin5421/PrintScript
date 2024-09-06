package formatter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class OptionsChecker {
  public static JsonObject checkAndReturn(String options) throws JsonSyntaxException {
    try {
      JsonObject rules = JsonParser.parseString(options).getAsJsonObject();
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
