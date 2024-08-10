package ast;

import java.util.List;

public class Program {
    private final List<ASTNode> statements;

    public Program(List<ASTNode> statements) {
        this.statements = statements;
    }

    public List<ASTNode> getStatements() {
        return statements;
    }
}
