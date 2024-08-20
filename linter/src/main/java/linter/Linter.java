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
            visitor = statement.accept(visitor);
        }

        String report = ((LinterVisitor) visitor).getReport();
        report = trimLastNewLine(report);
        return report;
    }

    private String trimLastNewLine(String report) {
        if (!report.isEmpty()) {
            if (report.charAt(report.length() - 1) == '\n') {
                String newReport = report.substring(0, report.length() - 1);
                return trimLastNewLine(newReport);
            }
        }
        return report;
    }
}
