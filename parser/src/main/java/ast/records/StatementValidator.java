package ast.records;

import ast.ASTNode;
import ast.Statement;
import ast.VariableDeclaration;
import ast.expressions.CallExpression;
import ast.expressions.ExpressionStatement;
import ast.expressions.ExpressionType;

public class StatementValidator {

    public static boolean isVariableDeclaration(ASTNode statement) {
        return statement instanceof VariableDeclaration;
    }

    public static boolean isExpressionStatement(ASTNode statement) {
        return statement instanceof ExpressionStatement;
    }

    public static boolean isCallExpression(ExpressionType statement) {
        return statement instanceof CallExpression;
    }
}
