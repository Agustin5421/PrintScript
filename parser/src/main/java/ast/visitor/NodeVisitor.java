package ast.visitor;

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
  NodeVisitor visitNumberLiteral(NumberLiteral numberLiteral);

  NodeVisitor visitStringLiteral(StringLiteral stringLiteral);

  NodeVisitor visitBooleanLiteral(BooleanLiteral booleanLiteral);

  NodeVisitor visitBinaryExpression(BinaryExpression binaryExpression);

  NodeVisitor visitCallExpression(CallExpression callExpression);

  NodeVisitor visitIdentifier(Identifier identifier);

  NodeVisitor visitAssignmentExpression(AssignmentExpression assignmentExpression);

  NodeVisitor visitVarDec(VariableDeclaration variableDeclaration);

  NodeVisitor visitIfStatement(IfStatement ifStatement);
}
