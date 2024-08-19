package linter;

import ast.root.ASTNode;
import ast.root.Program;
import visitors.ASTVisitor;
import visitors.LinterVisitor;

import java.util.List;

public class Linter {
    public String linter(Program program, String rules) {
        ASTVisitor visitor = new LinterVisitor(rules);

        List<ASTNode> statements = program.statements();

        for (ASTNode statement : statements) {
            visitor = statement.visit(visitor);
        }

        String report = ((LinterVisitor) visitor).getReport();
        report = trimLastNewLine(report);
        return report;
    }

    private static String trimLastNewLine(String report) {
        if (!report.isEmpty()) {
            report = report.substring(0, report.length() - 1);
        }
        return report;
    }
}
