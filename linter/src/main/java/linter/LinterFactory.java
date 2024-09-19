package linter;

import factory.ParserFactory;
import linter.visitor.LinterVisitor;
import linter.visitor.factory.LinterVisitorFactory;
import linter.visitor.factory.NewLinterVisitorFactory;
import linter.visitor.strategy.NewLinterVisitor;
import output.OutputResult;
import parsers.Parser;

public class LinterFactory {
  private static final LinterVisitorFactory linterVisitorFactory = new LinterVisitorFactory();
  private static final NewLinterVisitorFactory newLinterVisitorFactory =
      new NewLinterVisitorFactory();

  public static ReworkedLinter getReworkedLinter(
      String version, String rules, OutputResult<String> output) {
    NewLinterVisitor visitor = newLinterVisitorFactory.createLinterVisitor(version, rules, output);
    return new ReworkedLinter(visitor);
  }

  public static Linter getLinter(String version, String rules) {
    return switch (version) {
      case "1.0" -> getLinterV1(rules);
      case "1.1" -> getLinterV2(rules);
      default -> throw new IllegalArgumentException("Invalid version: " + version);
    };
  }

  private static Linter getLinterV1(String rules) {
    Parser parser = ParserFactory.getParser("1.0");

    LinterVisitor visitor = linterVisitorFactory.createLinterVisitor(rules);
    return new Linter(parser, visitor);
  }

  private static Linter getLinterV2(String rules) {
    Parser parser = ParserFactory.getParser("1.1");

    LinterVisitor visitor = linterVisitorFactory.createLinterVisitorV2(rules);
    return new Linter(parser, visitor);
  }
}
