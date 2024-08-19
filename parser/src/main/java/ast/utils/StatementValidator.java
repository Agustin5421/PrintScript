package ast.utils;

import ast.root.AstNode;
import ast.statements.CallExpression;
import ast.statements.VariableDeclaration;

public class StatementValidator {

  public static boolean isVariableDeclaration(AstNode statement) {
    return statement instanceof VariableDeclaration;
  }

  //    public static boolean isExpressionStatement(ASTNode statement) {
  //        return statement instanceof ExpressionStatement;
  //    }

  public static boolean isCallExpression(AstNode statement) {
    return statement instanceof CallExpression;
  }
}
