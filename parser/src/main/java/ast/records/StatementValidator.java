package ast.records;

import ast.ASTNode;
import ast.CallExpression;
import ast.VariableDeclaration;

public class StatementValidator {

    public static boolean isVariableDeclaration(ASTNode statement) {
        return statement instanceof VariableDeclaration;
    }

//    public static boolean isExpressionStatement(ASTNode statement) {
//        return statement instanceof ExpressionStatement;
//    }

    public static boolean isCallExpression(ASTNode statement) {
        return statement instanceof CallExpression;
    }
}
