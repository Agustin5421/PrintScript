package linter.rework;

import ast.root.AstNode;
import ast.root.Program;
import ast.visitor.NodeVisitor;
import java.util.List;
import linter.rework.report.FullReport;
import linter.rework.visitor.LinterVisitorFactory;
import linter.rework.visitor.LinterVisitorV2;

public class LinterV2 {
  private final LinterVisitorFactory linterVisitorFactory = new LinterVisitorFactory();

  public FullReport lint(Program program, String rules) {
    NodeVisitor linterVisitor = linterVisitorFactory.createLinterVisitor(rules);

    List<AstNode> statements = program.statements();
    for (AstNode statement : statements) {
      linterVisitor = statement.accept(linterVisitor);
    }

    return ((LinterVisitorV2) linterVisitor).getFullReport();
  }
}
