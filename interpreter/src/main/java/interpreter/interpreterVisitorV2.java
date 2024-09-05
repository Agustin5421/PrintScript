package interpreter;

import ast.root.AstNode;
import ast.visitor.NodeVisitor;

public class interpreterVisitorV2 extends interpreterVisitorV1{

    public interpreterVisitorV2(VariablesRepository variablesRepository) {
        super(variablesRepository);
    }

    @Override
    public NodeVisitor visit(AstNode node) {
        if (node instanceof ast.statements.VariableDeclaration) {
            return visitVarDec((ast.statements.VariableDeclaration) node);
        }
        return super.visit(node);
    }


}
