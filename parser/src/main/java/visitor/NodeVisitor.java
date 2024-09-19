package visitor;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.BooleanLiteral;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.IfStatement;
import ast.statements.VariableDeclaration;

public interface NodeVisitor {
  NodeVisitor visitVarDec(VariableDeclaration node);

  NodeVisitor visitIfStatement(IfStatement node);

  NodeVisitor visitBooleanLiteral(BooleanLiteral node);

  NodeVisitor visitCallExpression(CallExpression node);

  NodeVisitor visitAssignmentExpression(AssignmentExpression node);

  NodeVisitor visitBinaryExpression(BinaryExpression node);

  NodeVisitor visitNumberLiteral(NumberLiteral node);

  NodeVisitor visitStringLiteral(StringLiteral node);

  NodeVisitor visitIdentifier(Identifier node);
}
