package linter;

import linter.engine.LinterEngine;
import linter.engine.factory.LinterEngineFactory;
import output.OutputResult;

public class LinterFactory {
  private static final LinterEngineFactory linterEngineFactory = new LinterEngineFactory();

  public static Linter getReworkedLinter(
      String version, String rules, OutputResult<String> output) {
    LinterEngine engine = linterEngineFactory.createLinterEngine(version, rules, output);
    return new Linter(engine);
  }
}
