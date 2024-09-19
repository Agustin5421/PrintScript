package linter;

import linter.visitor.factory.NewLinterVisitorFactory;
import linter.visitor.strategy.NewLinterVisitor;
import output.OutputResult;

public class LinterFactory {
  private static final NewLinterVisitorFactory newLinterVisitorFactory =
      new NewLinterVisitorFactory();

  public static ReworkedLinter getReworkedLinter(
      String version, String rules, OutputResult<String> output) {
    NewLinterVisitor visitor = newLinterVisitorFactory.createLinterVisitor(version, rules, output);
    return new ReworkedLinter(visitor);
  }
}
