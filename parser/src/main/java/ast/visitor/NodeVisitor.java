package ast.visitor;

import ast.expressions.BinaryExpression;
import ast.identifier.Identifier;
import ast.literal.NumberLiteral;
import ast.literal.StringLiteral;
import ast.root.AstNode;
import ast.statements.AssignmentExpression;
import ast.statements.CallExpression;
import ast.statements.VariableDeclaration;

public interface NodeVisitor {
  NodeVisitor visitCallExpression(CallExpression callExpression);

  NodeVisitor visitAssignmentExpression(AssignmentExpression assignmentExpression);

  NodeVisitor visitVarDec(VariableDeclaration variableDeclaration);

  NodeVisitor visitNumberLiteral(NumberLiteral numberLiteral);

  NodeVisitor visitStringLiteral(StringLiteral stringLiteral);

  NodeVisitor visitIdentifier(Identifier identifier);

  NodeVisitor visitBinaryExpression(BinaryExpression binaryExpression);

  NodeVisitor visit(AstNode node);
}
