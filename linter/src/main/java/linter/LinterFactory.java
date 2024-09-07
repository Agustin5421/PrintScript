package linter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import factory.ParserFactory;
import linter.visitor.LinterVisitor;
import linter.visitor.factory.LinterVisitorFactory;
import parsers.Parser;

public class LinterFactory {
  private static final LinterVisitorFactory linterVisitorFactory = new LinterVisitorFactory();

  public static IterableLinter getLinter(String version, String rules) {
    return switch (version) {
      case "1.0" -> getLinterV1(rules);
      case "1.1" -> getLinterV2(rules);
      default -> throw new IllegalArgumentException("Invalid version: " + version);
    };
  }

  private static IterableLinter getLinterV1(String rules) {
    Parser parser = ParserFactory.getParser("1.0");

    boolean validateRulesV1 = validateRules(rules);

    if (!validateRulesV1) {
      throw new IllegalArgumentException("Invalid rules: " + rules);
    }

    LinterVisitor visitor = linterVisitorFactory.createLinterVisitor(rules);
    return new IterableLinter(parser, visitor);
  }

  private static IterableLinter getLinterV2(String rules) {
    Parser parser = ParserFactory.getParser("1.1");

    boolean validateRulesV1 = validateRules(rules);

    if (!validateRulesV1) {
      throw new IllegalArgumentException("Invalid rules: " + rules);
    }

    LinterVisitor visitor = linterVisitorFactory.createLinterVisitorV2(rules);
    return new IterableLinter(parser, visitor);
  }

  private static boolean validateRules(String rules) {
    try {
      JsonObject jsonObject = JsonParser.parseString(rules).getAsJsonObject();
      JsonObject idJson =
          jsonObject.getAsJsonObject("identifier").getAsJsonObject("writingConvention");
      idJson.get("conventionName").getAsString();
      idJson.get("conventionPattern").getAsString();
      jsonObject.getAsJsonObject("callExpression").getAsJsonArray("arguments");
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
