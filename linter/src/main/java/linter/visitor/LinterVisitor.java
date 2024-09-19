package linter.visitor;

import ast.root.AstNodeType;
import ast.visitor.NodeVisitor;
import java.util.Map;
import linter.visitor.report.FullReport;
import linter.visitor.strategy.LintingStrategy;
import output.OutputResult;

public interface LinterVisitor extends NodeVisitor {
  FullReport getFullReport();

  Map<AstNodeType, LintingStrategy> getNodesStrategies();

  default OutputResult<String> getOutput() {
    return null;
  }
}
