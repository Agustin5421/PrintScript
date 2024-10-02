package linter;

import linter.engine.LinterEngine;
import linter.engine.factory.LinterEngineFactory;
import output.OutputResult;
import report.Report;

public class LinterFactory {
  private static final LinterEngineFactory linterEngineFactory = new LinterEngineFactory();

  public static Linter getReworkedLinter(
      String version, String rules, OutputResult<Report> output) {
    LinterEngine engine = linterEngineFactory.createLinterEngine(version, rules, output);
    return new Linter(engine);
  }
}
