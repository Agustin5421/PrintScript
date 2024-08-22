package visitors;

import ast.identifier.Identifier;
import ast.literal.Literal;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.VariableDeclaration;

public interface ASTVisitor {
  ASTVisitor visitVarDec(VariableDeclaration node);

  ASTVisitor visitCallExpression(CallExpression node);

  ASTVisitor visitAssignmentExpression(AssignmentExpression node);

  ASTVisitor visitIdentifier(Identifier node);

  ASTVisitor visitLiteral(Literal<?> node);
}
