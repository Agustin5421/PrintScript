package linter;

import ast.root.ASTNode;
import ast.root.Program;
import visitors.ASTVisitor;
import visitors.LinterVisitor;

import java.util.List;

public class Linter {
    public String linter(Program program, String rules) {
        LinterVisitor visitor = new LinterVisitor(rules);

        List<ASTNode> statements = program.statements();

        for (ASTNode statement : statements) {
//            visitor = statement.visit(visitor);
        }

        return visitor.getReport();
    }
}
