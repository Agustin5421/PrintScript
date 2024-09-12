package formatter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class OptionsChecker {
  public static JsonObject checkAndReturn(String options, String version)
      throws JsonSyntaxException {
    switch (version) {
      case "1.1":
        return checkAndReturnV2(options);
      default:
        return checkAndReturnV1(options);
    }
  }

  public static JsonObject checkAndReturnV1(String options) {
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

  public static JsonObject checkAndReturnV2(String options) {
    JsonObject rules = checkAndReturnV1(options);
    try {
      rules.get("indentSize").getAsInt();
      return rules;
    } catch (Exception e) {
      throw new JsonSyntaxException("Invalid formatting options");
    }
  }
}
