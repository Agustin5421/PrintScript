package formatter.factory;

import com.google.gson.JsonObject;
import formatter.MainFormatter;
import formatter.OptionsChecker;
import output.OutputResult;

public class FormatterFactory {
  public static MainFormatter create(String options, String version, OutputResult<String> writer) {
    JsonObject rules = OptionsChecker.checkAndReturn(options, version);
    return new MainFormatter(EngineFactory.createEngine(version, rules, writer));
  }
}
